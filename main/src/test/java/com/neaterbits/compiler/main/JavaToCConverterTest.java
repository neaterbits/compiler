package com.neaterbits.compiler.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.neaterbits.compiler.c.emit.CCompilationUnitEmitter;
import com.neaterbits.compiler.common.ModuleId;
import com.neaterbits.compiler.common.SourceModuleSpec;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Module;
import com.neaterbits.compiler.common.ast.Program;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.BaseCompilationUnitEmitter;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ast.ProgramLoader;
import com.neaterbits.compiler.common.parser.ParsedFile;
import com.neaterbits.compiler.common.resolver.CodeMap;
import com.neaterbits.compiler.common.resolver.FilesResolver;
import com.neaterbits.compiler.common.resolver.ResolveFilesResult;
import com.neaterbits.compiler.common.resolver.ResolveLogger;
import com.neaterbits.compiler.common.resolver.UnresolvedDependencies;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.java.emit.JavaCompilationUnitEmitter;
import com.neaterbits.compiler.main.convert.ConvertClass;

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
		
		// Uses imports to resolve all type references to their class implementations that should now have been loaded

		// Also builds map of all extended by/extends relationships for classes and interfaces and methods thereof
		// This information will be used when figuring out how to do method dispatch for each call site 
		final ResolveFilesResult resolveResult = resolveFiles(program);
		
		final UnresolvedDependencies unresolved = resolveResult.getUnresolvedDependencies();
		if (!unresolved.isEmpty()) {
			throw new IllegalStateException("Unresolved dependencies " + unresolved);
		}
		
		final ResolvedType printstream = resolveResult.getResolvedFiles().stream()
				.flatMap(file -> file.getTypes().stream())
				.filter(type -> type.getFullTypeName().getName().getName().equals("PrintStream"))
				.findFirst().get();
				
		assertThat(printstream).isNotNull();
		
		assertThat(printstream.getExtendsFrom()).isNotNull();
		assertThat(printstream.getExtendsFrom().size()).isEqualTo(1);
		
		// Replaces all resolved type references within the AST
		replaceUnresolvedTypeReferences(resolveResult);
		
		// First map classes to C structs so can access between compilation units
		final Map<ComplexType<?>, StructType> complexToStruct = convertClassesAndInterfacesToStruct(resolveResult, new JavaToCClassToStructState());
		
		final CCompilationUnitEmitter emitter = new CCompilationUnitEmitter();
		
		final EmitterState emitterState = new EmitterState('\n');
		
		for (Module module : program.getModules()) {
			
			for (ParsedFile parsedFile : module.getParsedFiles()) {
				
				final CompilationUnit converted = convert(parsedFile.getParsed(), complexToStruct, resolveResult.getCodeMap());
				
				System.out.println("### converted code:");

				listCode(converted);
				
				emitCompilationUnit(converted, emitter, emitterState);
			}
		}
		
		System.out.println("Converted code:");
		
		System.out.println(emitterState.asString());
		
	}
	
	private <T extends EmitterState> void emitCompilationUnit(CompilationUnit compilationUnit, BaseCompilationUnitEmitter<T> emitter, T emitterState) {
		
		for (CompilationCode code : compilationUnit.getCode()) {
		
			System.out.println("## emit " + code);
			code.visit(emitter, emitterState);
			
			emitterState.newline();
		}
		
	}
	
	
	private ResolveFilesResult resolveFiles(Program program) {

		final ProgramLoader programLoader = new ProgramLoader(program);
		
		final ResolveLogger logger = new ResolveLogger(System.out);
		
		final FilesResolver resolver = new FilesResolver(logger);
		
		final Collection<CompiledFile> allFiles = programLoader.getAllFiles();
		
		for (CompiledFile compiledFile : allFiles) {
			System.out.println("File " + compiledFile.getSpec() + " with types " + compiledFile.getTypes());
		}
		
		return resolver.resolveFiles(allFiles);
	}
	
	@Test
	public void testConvertCompiler() throws IOException {
		
		final ModuleId common = new ModuleId("common");
		final ModuleId java = new ModuleId("java");

		final File baseDirectory = new File("..").getCanonicalFile();

		System.out.println("## baseDirectory: " + baseDirectory);

		final SourceModuleSpec commonModuleSpec = new SourceModuleSpec(
				common,
				Collections.emptyList(),
				new File(baseDirectory, "common/src/main/java"));
		
		final SourceModuleSpec javaModuleSpec = new SourceModuleSpec(
				java,
				Arrays.asList(commonModuleSpec),
				new File(baseDirectory, "java/src/main/java"));
		
		final Program program = parseProgram(Arrays.asList(commonModuleSpec));

		resolveFiles(program);
		
	/*
		final PrintStream logOutput = new PrintStream(new ByteArrayOutputStream());
		
		final List<ParsedFile> parsedFiles = directoryParser.parseDirectory(
				commonModuleSpec.getBaseDirectory(),
				new ParseLogger(logOutput));
		
		assertThat(parsedFiles.size()).isGreaterThan(0);
		*/
	}

	
	private CompilationUnit convert(CompilationUnit javaCompilationUnit, Map<ComplexType<?>, StructType> classToStruct, CodeMap codeMap) {
		final JavaToCConverter converter = new JavaToCConverter();

		final CompilationUnit cCode = converter.convertCompilationUnit(javaCompilationUnit, new JavaToCConverterState(classToStruct, codeMap));
		
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
