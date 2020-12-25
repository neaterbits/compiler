package com.neaterbits.compiler.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.objects.CompilationCode;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Import;
import com.neaterbits.compiler.ast.objects.ImportName;
import com.neaterbits.compiler.ast.objects.Namespace;
import com.neaterbits.compiler.ast.objects.NamespaceDeclaration;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.objects.type.complex.ComplexType;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.UnresolvedTypeReference;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.ProgramEmitter;
import com.neaterbits.compiler.java.JavaLexerObjectParser;
import com.neaterbits.compiler.java.ObjectJavaParser;
import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.main.lib.LibPlaceholder;
import com.neaterbits.compiler.resolver.build.LanguageCompiler;
import com.neaterbits.compiler.util.CastFullContextProvider;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.parsers.DirectoryParser;
import com.neaterbits.compiler.util.parse.parsers.FileTypeParser;
import com.neaterbits.compiler.util.parse.parsers.ProgramParser;
import com.neaterbits.compiler.util.parse.parsers.ProgramParser.Program;
import com.neaterbits.util.coll.MapOfList;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseJavaCompilerTest {

    private final CompilerCodeMap codeMap;
    
    BaseJavaCompilerTest() {

        this.codeMap = new IntCompilerCodeMap();
        
        LanguageCompiler.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);
    }
    
    private JavaLexerObjectParser<CompilationUnit> createParser() {
        
        return new JavaLexerObjectParser<>(
                ObjectJavaParser.createListener(codeMap::getTypeNoByTypeName));
    }

	final CompilationUnit compile(String fileName) throws IOException, ParserException {

		final JavaLexerObjectParser<CompilationUnit> parser = createParser();

		final List<ParseError> errors = new ArrayList<>();

		final CompilationUnit compilationUnit;

		final File file = new File(fileName);

		try (FileInputStream inputStream = new FileInputStream(file)) {

			compilationUnit = parser.parse(
			        inputStream,
			        Charset.defaultCharset(),
			        errors,
			        file.getName(),
			        new ParseLogger(System.out, CastFullContextProvider.INSTANCE));
		}

		assertThat(errors.isEmpty()).isTrue();
		assertThat(compilationUnit).isNotNull();

		return compilationUnit;
	}

	final ClassDefinition compileAndReturnClass(String fileName) throws IOException, ParserException {

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

	private static final Class<?> SYSTEM_LIB_PLACEHOLDER_CLASS = LibPlaceholder.class;

	private static final File SYSTEM_MODULE = 
			new File("src/main/java/" + getPackageDir(SYSTEM_LIB_PLACEHOLDER_CLASS));

	private static final File getSystemModule() {
		return SYSTEM_MODULE;
	}

	final Program<CompilationUnit, ASTParsedFile>
	parseProgram(MapOfList<File, File> modules) throws IOException {

		final FileTypeParser<CompilationUnit> javaParser = new FileTypeParser<>(createParser(), ".java");

		final DirectoryParser<CompilationUnit, ASTParsedFile> directoryParser
		        = new DirectoryParser<CompilationUnit, ASTParsedFile>(javaParser) {

                    @Override
                    protected ASTParsedFile createParsedFile(
                            FileSpec file,
                            List<CompileError> errors,
                            String log,
                            CompilationUnit parsed) {

                        return new ASTParsedFile(file, errors, log, parsed);
                    }
		};

		final ProgramParser<CompilationUnit, ASTParsedFile> programParser
		    = new ProgramParser<>(directoryParser);

		final Program<CompilationUnit, ASTParsedFile> program = programParser.parseProgram(
				modules,
				Charset.defaultCharset(),
				getSystemModule(),
				systemModule -> renameSystemPackages(systemModule, SYSTEM_LIB_PLACEHOLDER_CLASS.getPackage()),
				new ParseLogger(System.out, CastFullContextProvider.INSTANCE));

		assertThat(program).isNotNull();

		return program;
	}

	// Since we cannot override system packages, they are in a different package and we rename after compilation
	private void renameSystemPackages(Collection<ASTParsedFile> systemModule, Package basePackage) {
	    
	    for (ASTParsedFile systemModuleFile : systemModule) {
	        renameSystemPackages(systemModuleFile, basePackage);
	    }
	}

    private void renameSystemPackages(ASTParsedFile systemModuleFile, Package basePackage) {

		final String [] scopeToRename = Strings.split(basePackage.getName(), '.');

		systemModuleFile.iterateNodeFirst(e -> {
			if (e instanceof UnresolvedTypeReference) {
				final UnresolvedTypeReference typeReference = (UnresolvedTypeReference)e;

				if (typeReference.getScopedName().scopeStartsWith(scopeToRename)) {

					final ScopedName renamedScope = typeReference.getScopedName().removeFromScope(scopeToRename);

					typeReference.replaceWith(
							new UnresolvedTypeReference(
									typeReference.getContext(),
									renamedScope,
                                    typeReference.getGenericTypeParameters(),
									typeReference.getReferenceType(),
									typeReference.getNumPointers()));

				}
			}
			else if (e instanceof Import) {

				final Import importStatement = (Import)e;

				final ImportName importPackage = importStatement.getPackage();

				if (importPackage.startsWith(scopeToRename)) {
					importPackage.replaceWith(importPackage.removeFromNamespace(scopeToRename));
				}
			}
			else if (e instanceof Namespace) {
				final Namespace namespace = (Namespace)e;

				if (namespace.getReference().startsWith(scopeToRename)) {
					namespace.replaceWith(
							new Namespace(
									namespace.getContext(),
									namespace.getKeyword(),
									new NamespaceDeclaration(
											namespace.getNamespaceDeclaration().getContext(),
											namespace.getReference().removeFromNamespace(scopeToRename)),
									namespace.getLines().take()));
				}
			}
		});
        
    }

	static <T extends MappingJavaToCConverterState<T>>
	JavaToCDeclarations convertClassesAndInterfacesToStruct(
			CompilerCodeMap codeMap,
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
		        null,
				// resolveResult.getTypesInDependencyOrder(),
				codeMap,
				declarations,
				convertLaterTypeReferences,
				converterState);

		// References to not-yet resolved fields in types
		for (ComplexTypeReference reference : convertLaterTypeReferences) {

			final ComplexType<?, ?, ?> convertedStructType = declarations.getClassStructType(reference.getTypeName());

			if (convertedStructType == null) {
				throw new IllegalStateException("Non-converted type " + reference.getTypeName());
			}


			final ComplexTypeReference structReference = new ComplexTypeReference(reference.getContext(), -1, convertedStructType.getTypeName());

			reference.replaceWith(structReference);
		}

		return declarations;
	}

	private static <T extends MappingJavaToCConverterState<T>> void convertTypes(
			Collection<TypeName> types,
			CompilerCodeMap codeMap,
			JavaToCDeclarations declarations,
			List<ComplexTypeReference> convertLaterTypeReferences,
			MappingJavaToCConverterState<T> converterState) {


		for (TypeName resolvedType : types) {

			// if (resolvedType.getTypeVariant() == TypeVariant.CLASS) {

				throw new UnsupportedOperationException();

				/*
				final ClassDefinition classType = (ClassType)resolvedType.getType();

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
				*/
			// }
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
