package com.neaterbits.compiler.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.c.emit.CCompilationUnitEmitter;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.base.BaseCompilationUnitEmitter;
import com.neaterbits.compiler.java.JavaProgramModel;
import com.neaterbits.compiler.java.JavaTypes;
import com.neaterbits.compiler.java.emit.JavaCompilationUnitEmitter;
import com.neaterbits.compiler.main.convert.ConvertClass;
import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.FilesResolver;
import com.neaterbits.compiler.resolver.AddToCodeMapResult;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolveLogger;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMap;
import com.neaterbits.compiler.resolver.ResolverLibraryTypes;
import com.neaterbits.compiler.resolver.UnresolvedDependencies;
import com.neaterbits.compiler.resolver.ast.ASTModelImpl;
import com.neaterbits.compiler.resolver.ast.ProgramLoader;
import com.neaterbits.compiler.resolver.passes.AddToCodeMapPass;
import com.neaterbits.compiler.resolver.passes.ReplaceResolvedTypeReferencesPass;
import com.neaterbits.compiler.resolver.passes.ResolvedTypeDependencies;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.modules.ModuleId;
import com.neaterbits.compiler.util.modules.SourceModuleSpec;
import com.neaterbits.compiler.util.passes.ParsedFiles;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaToCConverterTest extends BaseJavaCompilerTest {

	@Test
	public void testIfStatements() throws IOException {

		// final String fileName = "src/test/java/com/neaterbits/compiler/main/JavaToCConverterTest.java";
		//final String fileName = "../common/src/main/java/com/neaterbits/compiler/common/ModuleSpec.java";
		
		final String fileName = "src/test/java/com/neaterbits/compiler/main/IfStatementTestClass.java";

		final CompilationUnit compilationUnit = compile(fileName);
		
		final JavaCompilationUnitEmitter javaEmitter = new JavaCompilationUnitEmitter();
		
		final String emitted = emitCompilationUnit(compilationUnit, javaEmitter);
		
		System.out.println("Java code:\n" + emitted);
	}

	@Test
	public void testConvertCode() throws IOException {
		
		final SourceModuleSpec moduleSpec = new SourceModuleSpec(new ModuleId("testconvert"), null, new File("src/test/java/" + getPackageDir(ConvertClass.class)));

		final Program program = parseProgram(Arrays.asList(moduleSpec));

		assertThat(program).isNotNull();
		
		listProgram(program);

		final ASTModelImpl astModel = new ASTModelImpl();

		// Uses imports to resolve all type references to their class implementations that should now have been loaded

		// Also builds map of all extended by/extends relationships for classes and interfaces and methods thereof
		// This information will be used when figuring out how to do method dispatch for each call site 
		final ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveResult = resolveFiles(program, null, astModel);
		
		final UnresolvedDependencies unresolved = resolveResult.getUnresolvedDependencies();
		if (!unresolved.isEmpty()) {
			throw new IllegalStateException("Unresolved dependencies " + unresolved);
		}

		System.out.println("## resolved files: " + resolveResult.getResolvedFiles());
		
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> printstream = resolveResult.getResolvedFiles().stream()
				.flatMap(file -> file.getTypes().stream())
				.filter(type -> type.getTypeName().equals("PrintStream"))
				.findFirst().get();
				
		assertThat(printstream).isNotNull();
		
		assertThat(printstream.getExtendsFrom()).isNotNull();
		assertThat(printstream.getExtendsFrom().size()).isEqualTo(1);
		
		ReplaceResolvedTypeReferencesPass.replaceResolvedTypeReferences(resolveResult, scopedName -> null, astModel);
		
		final List<ASTParsedFile> astParsedFiles = new ArrayList<>();
		
		program.getModules().forEach(module -> module.getParsedFiles().forEach(astParsedFiles::add));
		
		final ParsedFiles<ASTParsedFile> parsedFiles = new ParsedFiles<>(astParsedFiles);
		
		final ResolvedTypeDependencies<ASTParsedFile, BuiltinType, ComplexType<?, ?, ?>, TypeName> resolved =
				new ResolvedTypeDependencies<>(parsedFiles, resolveResult);
		
		// Replaces all resolved type references within the AST
		final AddToCodeMapResult<ASTParsedFile, BuiltinType, ComplexType<?, ?, ?>, TypeName> replaceTypeReferencesResult
				= AddToCodeMapPass.makeCodeMap(resolved, astModel);
		
		// First map classes to C structs so can access between compilation units
		final JavaToCDeclarations declarations = convertClassesAndInterfacesToStruct(replaceTypeReferencesResult, new JavaToCClassToStructState());
		
		final CCompilationUnitEmitter emitter = new CCompilationUnitEmitter();
		
		final EmitterState emitterState = new EmitterState('\n');
		
		final List<CompilationUnit> compilationUnits = new ArrayList<>();
		
		for (Module module : program.getModules()) {
			
			for (ASTParsedFile parsedFile : module.getParsedFiles()) {
				
				final CompilationUnit converted = convert(parsedFile.getParsed(), declarations, replaceTypeReferencesResult.getCodeMap());
				
				System.out.println("### converted code:");

				listCode(converted);
				
				//emitCompilationUnit(converted, emitter, emitterState);
				
				compilationUnits.add(converted);
			}
		}
		
		final CompilationUnit declarationsCompilationUnit = makeDeclarationsCompilationUnit(declarations);
		
		emitCompilationUnit(declarationsCompilationUnit, emitter, emitterState);
		
		for (CompilationUnit compilationUnit : compilationUnits) {
			emitCompilationUnit(compilationUnit, emitter, emitterState);
		}
		
		System.out.println("Converted code:");
		
		System.out.println(emitterState.asString());
		
	}
	
	private static CompilationUnit makeDeclarationsCompilationUnit(JavaToCDeclarations declarations) {
		
		final List<CompilationCode> compilationCode = new ArrayList<>();
		
		for (JavaToCClassDeclaration declaration : declarations.getDeclarations()) {
			
			compilationCode.add(declaration.getDataFieldStructType().getDefinition());
			
			compilationCode.add(declaration.getVTableStructType().getDefinition());
		}
		
		final CompilationUnit compilationUnit = new CompilationUnit(
				new Context("declarations", 0, 0, 0, 0, 0, 0, null),
				Collections.emptyList(),
				compilationCode);

		return compilationUnit;
	}
	
	private <T extends EmitterState> void emitCompilationUnit(CompilationUnit compilationUnit, BaseCompilationUnitEmitter<T> emitter, T emitterState) {
		
		for (CompilationCode code : compilationUnit.getCode()) {
		
			System.out.println("## emit " + code);
			code.visit(emitter, emitterState);
			
			emitterState.newline();
		}
	}
	
	private ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveFiles(
			Program program,
			ResolverLibraryTypes<TypeName> libraryTypes,
			ASTTypesModel<BuiltinType, ComplexType<?, ?, ?>, TypeName> astModel) {

		final ResolveLogger<BuiltinType, ComplexType<?, ?, ?>, TypeName, CompilationUnit>
			logger = new ResolveLogger<>(System.out);
		
		final FilesResolver<BuiltinType, ComplexType<?, ?, ?>, TypeName, CompilationUnit> resolver = new FilesResolver<>(
				logger,
				JavaTypes.getBuiltinTypes(),
				libraryTypes,
				new JavaProgramModel(),
				astModel);
		
		final Collection<CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>> allFiles = ProgramLoader.getCompiledFiles(program);
		
		for (CompiledFile<ComplexType<?, ?, ?>, CompilationUnit> compiledFile : allFiles) {
			System.out.println("File " + compiledFile.getSpec() + " with types " + compiledFile.getTypes());
		}
		
		return resolver.resolveFiles(allFiles);
	}
	
	@Test
	public void testConvertCompiler() throws IOException {
		
		final ModuleId common = new ModuleId("common");
		// final ModuleId java = new ModuleId("java");

		final File baseDirectory = new File("..").getCanonicalFile();

		System.out.println("## baseDirectory: " + baseDirectory);

		final SourceModuleSpec commonModuleSpec = new SourceModuleSpec(
				common,
				Collections.emptyList(),
				new File(baseDirectory, "common/src/main/java"));
		
		/*
		final SourceModuleSpec javaModuleSpec = new SourceModuleSpec(
				java,
				Arrays.asList(commonModuleSpec),
				new File(baseDirectory, "java/src/main/java"));
		*/
		
		final Program program = parseProgram(Arrays.asList(commonModuleSpec));

		final ASTModelImpl astModel = new ASTModelImpl();
		
		resolveFiles(program, null, astModel);
		
	/*
		final PrintStream logOutput = new PrintStream(new ByteArrayOutputStream());
		
		final List<ParsedFile> parsedFiles = directoryParser.parseDirectory(
				commonModuleSpec.getBaseDirectory(),
				new ParseLogger(logOutput));
		
		assertThat(parsedFiles.size()).isGreaterThan(0);
		*/
	}

	
	private CompilationUnit convert(
			CompilationUnit javaCompilationUnit,
			JavaToCDeclarations declarations,
			ResolvedTypeCodeMap<BuiltinType, ComplexType<?, ?, ?>, TypeName> codeMap) {
		
		final JavaToCConverter converter = new JavaToCConverter();

		final CompilationUnit cCode = converter.convertCompilationUnit(javaCompilationUnit, new JavaToCConverterState(declarations, codeMap));
		
		return cCode;
	}
	
	private void listProgram(Program program) {
		listCode(program);
	}
	
	private void listCode(BaseASTElement code) {
		code.iterateNodeFirstWithStack((element, stack) -> {
			System.out.println(Strings.indent(stack.size()) + element.getClass().getSimpleName());
		});
	}
}
