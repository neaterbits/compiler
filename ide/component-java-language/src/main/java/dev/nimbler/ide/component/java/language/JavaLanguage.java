package dev.nimbler.ide.component.java.language;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jutils.Strings;
import org.jutils.parse.ParserException;

import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.common.tasks.util.SourceFileScanner;
import dev.nimbler.build.language.java.jdk.JavaBuildableLanguage;
import dev.nimbler.build.strategies.compilemodules.CompileModule;
import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.build.strategies.compilemodules.ResolvedModule;
import dev.nimbler.build.types.resource.ModuleResourcePath;
import dev.nimbler.build.types.resource.NamespaceResource;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResource;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.parser.ASTParsedFile;
import dev.nimbler.compiler.language.java.JavaLanguageSpec;
import dev.nimbler.compiler.language.java.JavaTypes;
import dev.nimbler.compiler.model.common.BaseTypeVisitor;
import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.model.common.ResolvedTypes;
import dev.nimbler.compiler.model.common.TypeMemberVisitor;
import dev.nimbler.compiler.model.objects.ObjectProgramModel;
import dev.nimbler.compiler.model.objects.ObjectsCompilerModel;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.build.CompileSource;
import dev.nimbler.compiler.resolver.build.CompilerOptions;
import dev.nimbler.compiler.resolver.build.ModulesBuilder;
import dev.nimbler.compiler.resolver.build.ResolvedSourceModule;
import dev.nimbler.compiler.resolver.build.SourceBuilder;
import dev.nimbler.compiler.resolver.build.SourceModule;
import dev.nimbler.compiler.util.parse.CompileError;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.language.compilercommon.CompilerSourceFileModel;
import dev.nimbler.ide.component.common.language.model.ParseableLanguage;
import dev.nimbler.ide.component.common.runner.RunnableLanguage;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.MethodVariant;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.Visibility;

public final class JavaLanguage
        extends JavaBuildableLanguage
        implements
        CompileableLanguage, ParseableLanguage, RunnableLanguage {

    private static final CompilerOptions COMPILER_OPTIONS = new CompilerOptions(true);
    
    private static final LanguageSpec LANGUAGE_SPEC = JavaLanguageSpec.INSTANCE;

	private static NamespaceResource getNamespace(SourceFileResourcePath sourceFile) {
		
		final NamespaceResource namespaceResource;
		
		if (sourceFile.getFromLast(1) instanceof NamespaceResource) {
			namespaceResource = (NamespaceResource)sourceFile.getFromLast(1);
		}
		else {
			
			final SourceFolderResource sourceFolder = (SourceFolderResource)sourceFile.getFromLast(1);
			
			namespaceResource = SourceFileScanner.getNamespaceResource(sourceFolder.getFile(), sourceFile.getFile()).getNamespace();
		}
		
		return namespaceResource;
	}
	
	public static File getSystemJarFilePath(String libName) {
		
		final String jreDir = System.getProperty("java.home");
		
		return new File(jreDir + "/lib/" + libName);
	}
	
	@Override
	public TypeName getTypeName(SourceFileResourcePath sourceFile) {

		return new TypeName(
				getNamespace(sourceFile).getNamespace(),
				null,
				classNameStringFromSourceFile(sourceFile.getFile()));
	}
	
	@Override
	public TypeName getTypeName(String namespace, String name) {

		final String [] parts = namespace != null && !namespace.isEmpty()
					? Strings.split(namespace, '.')
					: null; 
					
		return new TypeName(parts, null, name);
	}
	
	@Override
	public String getNamespaceString(TypeName typeName) {
		
		Objects.requireNonNull(typeName);
		Objects.requireNonNull(typeName.getNamespace());
		
		return Strings.join(typeName.getNamespace(), '.');
	}
	

	@Override
	public String getCompleteNameString(TypeName typeName) {

		Objects.requireNonNull(typeName);
		
		return typeName.join('.');
	}

	@Override
	public String getBinaryName(TypeName typeName) {
		return typeName.getName() + ".class";
	}

	private static String classNameStringFromSourceFile(File file) {
		
		final String name = file.getName();
		
		return name.substring(0, name.length() - ".java".length());
	}
	
	private Charset getCharset() {
	    
	    return Charset.defaultCharset();
	}

	private static ObjectsCompilerModel makeCompilerModel(CompilerCodeMap codeMap) {
	    
	    return new ObjectsCompilerModel(
                LANGUAGE_SPEC,
                JavaTypes.getBuiltinTypes(),
                codeMap::getTypeNoByTypeName);
	}

	@Override
	public Map<SourceFileResourcePath, SourceFileModel> parseModule(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> files,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException {

		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels = new HashMap<>(files.size());
		
		final ObjectsCompilerModel compilerModel = makeCompilerModel(codeMap);
		        
		final ModulesBuilder<CompilationUnit, ASTParsedFile> modulesBuilder
		    = new ModulesBuilder<>(LANGUAGE_SPEC, compilerModel, COMPILER_OPTIONS);
		
		final CompileModule compileModule
		    = new CompileModule(
		            (ProjectModuleResourcePath)modulePath,
		            files,
		            getCharset(),
		            Collections.emptyList(),
		            Collections.emptyList());
		
		final ResolvedModule<ASTParsedFile, ResolveError> resolvedModule;
		
		try {
            resolvedModule = modulesBuilder.compile(compileModule, codeMap);
        } catch (IOException | ParserException ex) {
            throw new IllegalStateException(ex);
        }

		for (ParsedWithCachedRefs<ASTParsedFile, ResolveError> parsedWithCachedRefs
		            : resolvedModule.getParsedModule().getParsed()) {

		    
		    final SourceFileModel sourceFileModel = makeSourceFileModel(
		                                                parsedWithCachedRefs,
		                                                resolvedTypes,
		                                                codeMap);
		    
	        final SourceFileResourcePath path = resolvedModule.getCompileModule().getSourceFiles().stream()
	                .filter(sourceFile -> sourceFile.getFile().equals(
	                                            parsedWithCachedRefs.getParsedFile().getFileSpec().getFile()))
	                .findFirst()
	                .orElseThrow(IllegalStateException::new);

	        sourceFileModels.put(path, sourceFileModel);
		}
		
		return sourceFileModels;
	}
	
	@Override
	public SourceFileModel parseAndResolveChangedFile(
			SourceFileResourcePath sourceFilePath,
			String string,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) {

		final ObjectsCompilerModel compilerModel = makeCompilerModel(codeMap);

		final SourceBuilder<CompilationUnit, ASTParsedFile> sourceBuilder
                                                    		    = new SourceBuilder<>(
                                                    		            LANGUAGE_SPEC,
                                                    		            compilerModel,
                                                    		            COMPILER_OPTIONS);
		
		final CompileSource compileSource = new CompileSource(string, sourceFilePath.getFile().getName());
		
		final SourceModule sourceModule = new SourceModule(
		        Arrays.asList(compileSource),
		        getCharset(),
		        Collections.emptyList(),
		        Collections.emptyList());

		final ResolvedSourceModule<ASTParsedFile> resolvedSourceModule;

        try {
            resolvedSourceModule = sourceBuilder.compile(
                                                    sourceModule,
                                                    codeMap,
                                                    fileName -> sourceFilePath.getFile());

        } catch (IOException | ParserException ex) {
            throw new IllegalStateException(ex);
        }
		
		final ParsedWithCachedRefs<ASTParsedFile, ResolveError> parsedWithCachedRefs
		        = resolvedSourceModule.getParsedModule().getParsed().get(0);

		final SourceFileModel sourceFileModel = makeSourceFileModel(
		                                                parsedWithCachedRefs,
		                                                resolvedTypes,
		                                                codeMap);
		
		return sourceFileModel;
	}

    private SourceFileModel makeSourceFileModel(
            ParsedWithCachedRefs<ASTParsedFile, ResolveError> parsedWithCachedRefs,
            ResolvedTypes resolvedTypes,
            CompilerCodeMap codeMap) {
        
        final List<CompileError> allErrors = new ArrayList<>();
        
        allErrors.addAll(parsedWithCachedRefs.getParsedFile().getErrors());
        allErrors.addAll(parsedWithCachedRefs.getResolveErrorsList());
        
        final SourceFileModel sourceFileModel = new CompilerSourceFileModel(
                new ObjectProgramModel(),
                parsedWithCachedRefs.getParsedFile().getParsed(),
                allErrors,
                resolvedTypes,
                parsedWithCachedRefs.getCodeMapFileNo(),
                codeMap);
        
        return sourceFileModel;
    }
    
    private static class IsRunnableVisitor extends BaseTypeVisitor implements TypeMemberVisitor {
        
        private boolean isRunnable;

        @Override
        public void onField(CharSequence name, TypeName type, int numArrayDimensions, boolean isStatic,
                Visibility visibility, Mutability mutability, boolean isVolatile, boolean isTransient,
                int indexInType) {

            
        }

        @Override
        public void onMethod(
                String name,
                MethodVariant methodVariant,
                TypeName returnType,
                TypeName[] parameterTypes,
                int indexInType) {
            
            if (isMainMethod(name, methodVariant, parameterTypes.length)) {
                this.isRunnable = true;
            }
        }
    }
    
	@Override
    public boolean isSourceFileRunnable(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel) {

        final IsRunnableVisitor visitor = new IsRunnableVisitor();
        
        sourceFileModel.iterateTypeMembers(visitor);

        return visitor.isRunnable;
    }

    @Override
    public TypeName getRunnableType(ClassBytecode bytecode) {

        if (!isBytecodeRunnable(bytecode)) {
            throw new IllegalArgumentException();
        }
        
        return bytecode.getTypeName();
    }

    @Override
    public boolean isBytecodeRunnable(ClassBytecode bytecode) {

        boolean isRunnable = false;
        
        for (int i = 0; i < bytecode.getMethodCount(); ++ i) {
            
            if (isMainMethod(
                    bytecode.getMethodName(i),
                    bytecode.getMethodVariant(i),
                    bytecode.getMethodParameterCount(i))) {
                
                isRunnable = true;
                break;
            }
        }
        
        return isRunnable;
    }

    private static boolean isMainMethod(String name, MethodVariant methodVariant, int parameterCount) {

        return     name.equals("main")
                && methodVariant == MethodVariant.STATIC
                && parameterCount == 1;
    }
}

