package com.neaterbits.compiler.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Import;
import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.DirectoryParser;
import com.neaterbits.compiler.ast.parser.FileTypeParser;
import com.neaterbits.compiler.ast.parser.ProgramParser;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.complex.StructType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.convert.ootofunction.ClassToFunctionsConverter;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.ProgramEmitter;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.main.lib.LibPlaceholder;
import com.neaterbits.compiler.resolver.ReplaceTypeReferencesResult;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMap;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.modules.ModuleId;
import com.neaterbits.compiler.util.modules.ModuleSpec;
import com.neaterbits.compiler.util.modules.SourceModuleSpec;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class BaseJavaCompilerTest {
	
	final CompilationUnit compile(String fileName) throws IOException {
		final Java8AntlrParser parser = new Java8AntlrParser(true);
		final List<ParseError> errors = new ArrayList<>();

		final CompilationUnit compilationUnit;

		final File file = new File(fileName);
		
		try (FileInputStream inputStream = new FileInputStream(file)) {
			compilationUnit = parser.parse(inputStream, errors, file.getName(), new ParseLogger(System.out));
		}

		assertThat(errors.isEmpty()).isTrue();
		assertThat(compilationUnit).isNotNull();
		
		return compilationUnit;
	}

	final ClassDefinition compileAndReturnClass(String fileName) throws IOException {
		
		final CompilationUnit compilationUnit = compile(fileName);
		
		assertThat(compilationUnit.getCode().size()).isEqualTo(1);
		
		final CompilationCode code = compilationUnit.getCode().iterator().next();
		
		assertThat(code instanceof Namespace).isTrue();
		
		final Namespace nameSpace = (Namespace)code;
		
		assertThat(nameSpace.getLines()).isNotNull();
		assertThat(nameSpace.getLines().getCode().size()).isEqualTo(1);
		
		final CompilationCode namespaceCode = nameSpace.getLines().getCode().iterator().next();
		
		assertThat(namespaceCode instanceof ClassDefinition).isTrue();

		return (ClassDefinition)namespaceCode;
	}

	static String getPackageDir(Class<?> cl) {

		final String [] path = Strings.split(cl.getPackage().getName(), '.');
		final String baseDir = Strings.join(path, '/');

		return baseDir;
	}

	private static final String SYSTEM_MODULE_ID = "system";

	private static final Class<?> SYSTEM_LIB_PLACEHOLDER_CLASS = LibPlaceholder.class;
	
	private static final SourceModuleSpec SYSTEM_MODULE = new SourceModuleSpec(
			new ModuleId(SYSTEM_MODULE_ID),
			null,
			new File("src/main/java/" + getPackageDir(SYSTEM_LIB_PLACEHOLDER_CLASS)));

	private static final SourceModuleSpec getSystemModule() {
		return SYSTEM_MODULE;
	}
	
	final Program parseProgram(List<ModuleSpec> modules) throws IOException {
		
		final FileTypeParser<JavaParserListener> javaParser = new FileTypeParser<>(
				new Java8AntlrParser(true),
				logger -> new JavaParserListener(logger), 
				".java");

		final DirectoryParser directoryParser = new DirectoryParser(javaParser);
		
		final ProgramParser programParser = new ProgramParser(directoryParser);
		
		final Program program = programParser.parseProgram(
				modules,
				getSystemModule(),
				systemModule -> renameSystemPackages(systemModule, SYSTEM_LIB_PLACEHOLDER_CLASS.getPackage()),
				new ParseLogger(System.out));
		
		assertThat(program).isNotNull();

		return program;
	}
	
	// Since we cannot override system packages, they are in a different package and we rename after compilation
	private final void renameSystemPackages(Module systemModule, Package basePackage) {
		
		final String [] scopeToRename = Strings.split(basePackage.getName(), '.');

		systemModule.iterateNodeFirst(e -> {
			if (e instanceof ResolveLaterTypeReference) {
				final ResolveLaterTypeReference typeReference = (ResolveLaterTypeReference)e;
				
				if (typeReference.getTypeName().scopeStartsWith(scopeToRename)) {
			
					final ScopedName renamedScope = typeReference.getTypeName().removeFromScope(scopeToRename);
					
					typeReference.replaceWith(
							new ResolveLaterTypeReference(
									typeReference.getContext(),
									renamedScope));
					
				}
			}
			else if (e instanceof Import) {
				
				final Import importStatement = (Import)e;

				if (importStatement.startsWith(scopeToRename)) {
					importStatement.replaceWith(importStatement.removeFromNamespace(scopeToRename));
				}
			}
			else if (e instanceof Namespace) {
				final Namespace namespace = (Namespace)e;
				
				if (namespace.getReference().startsWith(scopeToRename)) {
					namespace.replaceWith(
							new Namespace(
									namespace.getContext(),
									namespace.getReference().removeFromNamespace(scopeToRename),
									namespace.getLines().take()));
				}
			}
		});
	}
	
	
	static <T extends MappingJavaToCConverterState<T>>
	JavaToCDeclarations convertClassesAndInterfacesToStruct(
			ReplaceTypeReferencesResult<BuiltinType, ComplexType<?, ?, ?>> resolveResult,
			MappingJavaToCConverterState<T> converterState) {
		
		final JavaToCDeclarations declarations = new JavaToCDeclarations();
		
		final List<ComplexTypeReference> convertLaterTypeReferences = new ArrayList<>();

		/*
		for (ResolvedFile file : resolveResult.getResolvedFiles()) {
			convertTypes(
					file.getTypes(),
					resolveResult.getCodeMap(),
					classToStruct,
					classToVTable,
					convertLaterTypeReferences,
					converterState);
		}
		 */
		
		convertTypes(
				resolveResult.getTypesInDependencyOrder(),
				resolveResult.getCodeMap(),
				declarations,
				convertLaterTypeReferences,
				converterState);

		// References to not-yet resolved fields in types
		for (ComplexTypeReference reference : convertLaterTypeReferences) {
		
			final ComplexType<?, ?, ?> convertedStructType = declarations.getClassStructType((ClassType)reference.getType());
			
			if (convertedStructType == null) {
				final NamedType namedType = (NamedType)reference.getType();
				
				throw new IllegalStateException("Non-converted type " + namedType.getName());
			}
			
			
			final ComplexTypeReference structReference = new ComplexTypeReference(reference.getContext(), convertedStructType);

			reference.replaceWith(structReference);
		}
		
		return declarations;
	}
	
	private static <T extends MappingJavaToCConverterState<T>> void convertTypes(
			Collection<ResolvedType<BuiltinType, ComplexType<?, ?, ?>>> types,
			ResolvedTypeCodeMap<BuiltinType, ComplexType<?, ?, ?>> codeMap,
			JavaToCDeclarations declarations,
			List<ComplexTypeReference> convertLaterTypeReferences,
			MappingJavaToCConverterState<T> converterState) {
		
		
		for (ResolvedType<BuiltinType, ComplexType<?, ?, ?>> resolvedType : types) {
			
			if (resolvedType.getTypeVariant() == TypeVariant.CLASS) {
				
				final ClassType classType = (ClassType)resolvedType.getType();
			
				final StructType classStructType = ClassToFunctionsConverter.convertClassFieldsToStruct(
						classType,
						declarations,
						convertLaterTypeReferences,
						fieldType -> converterState.convertTypeReference(fieldType),
						converterState::classToStructName);
				
				if (classStructType == null) {
					throw new IllegalStateException();
				}
				
				final StructType vtableStructType = ClassToFunctionsConverter.convertClassMethodsToVTable(
						classType,
						declarations,
						codeMap,
						methodType -> converterState.convertType(methodType),
						converterState::classToStructName,
						baseType -> converterState.getVTableBaseFieldName(baseType),
						methodName -> converterState.getVTableFunctionFieldName(methodName)); 
				
				declarations.add(new JavaToCClassDeclaration(classType, classStructType, vtableStructType));
			}
		}
	}

	static String emitCompilationUnit(CompilationUnit compilationUnit, ProgramEmitter<EmitterState> emitter) {
		final EmitterState emitterState = new EmitterState('\n');

		for (CompilationCode code : compilationUnit.getCode()) {
			code.visit(emitter, emitterState);
		}

		return emitterState.asString();		
	}
}
