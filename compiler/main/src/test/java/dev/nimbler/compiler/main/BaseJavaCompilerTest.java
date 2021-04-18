package dev.nimbler.compiler.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jutils.Strings;
import org.jutils.coll.MapOfList;
import org.jutils.parse.ParserException;

import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.Import;
import dev.nimbler.compiler.ast.objects.ImportName;
import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.NamespaceDeclaration;
import dev.nimbler.compiler.ast.objects.parser.ASTParsedFile;
import dev.nimbler.compiler.ast.objects.type.complex.ComplexType;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typereference.ComplexTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.UnresolvedTypeReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.ProgramEmitter;
import dev.nimbler.compiler.language.java.JavaLanguageSpec;
import dev.nimbler.compiler.language.java.JavaTypes;
import dev.nimbler.compiler.main.lib.LibPlaceholder;
import dev.nimbler.compiler.model.objects.ObjectsCompilerModel;
import dev.nimbler.compiler.resolver.build.ModulesBuilder;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.parse.CompileError;
import dev.nimbler.compiler.util.parse.ParseError;
import dev.nimbler.compiler.util.parse.ParseLogger;
import dev.nimbler.compiler.util.parse.Parser;
import dev.nimbler.compiler.util.parse.parsers.DirectoryParser;
import dev.nimbler.compiler.util.parse.parsers.FileTypeParser;
import dev.nimbler.compiler.util.parse.parsers.ProgramParser;
import dev.nimbler.compiler.util.parse.parsers.ProgramParser.Program;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

public abstract class BaseJavaCompilerTest {

    private final CompilerCodeMap codeMap;
    
    BaseJavaCompilerTest() {

        this.codeMap = new IntCompilerCodeMap();
        
        ModulesBuilder.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);
    }
    
    private Parser<CompilationUnit> createParser() {
        
        final JavaLanguageSpec languageSpec = JavaLanguageSpec.INSTANCE;
        
        final ObjectsCompilerModel compilerModel = new ObjectsCompilerModel(
                languageSpec,
                JavaTypes.getBuiltinTypes(),
                codeMap::getTypeNoByTypeName);

        final Parser<CompilationUnit> parser
            = languageSpec.createParser(compilerModel);
        
        return parser;
    }
    
	final CompilationUnit compile(String fileName) throws IOException, ParserException {
	    
	    final Parser<CompilationUnit> parser = createParser();
	    
		final List<ParseError> errors = new ArrayList<>();

		final CompilationUnit compilationUnit;

		final File file = new File(fileName);

		try (FileInputStream inputStream = new FileInputStream(file)) {

			compilationUnit = parser.parse(
			        inputStream,
			        Charset.defaultCharset(),
			        errors,
			        file.getName(),
			        fullContextProvider -> new ParseLogger(System.out, fullContextProvider));
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
				fullContextProvider -> new ParseLogger(System.out, fullContextProvider));

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

		systemModuleFile.getParsed().iterateNodeFirst(e -> {
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
