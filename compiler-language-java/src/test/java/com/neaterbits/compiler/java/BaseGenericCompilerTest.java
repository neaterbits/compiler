package com.neaterbits.compiler.java;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.strategies.compilemodules.CompileModule;
import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.build.strategies.compilemodules.ResolvedModule;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.language.Language;
import com.neaterbits.build.types.resource.ModuleResource;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileHolderResourcePath;
import com.neaterbits.build.types.resource.SourceFileResource;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResource;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.CompileFileCollector.CompileFile;
import com.neaterbits.compiler.java.resolve.TestResolvedTypes;
import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.CompilerOptions;
import com.neaterbits.compiler.resolver.build.ModulesBuilder;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.types.FieldInfo;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.FileSystemFileSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.util.IOUtils;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseGenericCompilerTest<
            COMPILATION_UNIT,
            PARSED_FILE extends ParsedFile> {
    
    protected abstract CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> 
        createCompilerLanguage(GetBuiltinTypeNo getBuiltinTypeNo);

	protected final CompiledAndResolvedFile compile(String file, String text, TestResolvedTypes resolvedTypes) throws IOException, ParserException {
		
		Objects.requireNonNull(file);

		final TestFile testFile = new TestFile(file, text);

		return compile(testFile, resolvedTypes);
	}

	protected final CompiledAndResolvedFile compile(TestFile testFile, TestResolvedTypes resolvedTypes)
	        throws IOException, ParserException {
	    
		Objects.requireNonNull(testFile);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(testFile)
				.compile(resolvedTypes)
				.getFile(testFile.getName());
		
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileAndMap(
	        TestFile testFile,
	        TestResolvedTypes resolvedTypes,
            IntCompilerCodeMap codeMap) throws IOException, ParserException {
	    
	    return compileAndMap(testFile.getName(), testFile.getText(), resolvedTypes, codeMap);
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileAndMap(
			String name,
			String text,
			TestResolvedTypes resolvedTypes,
			IntCompilerCodeMap codeMap) throws IOException, ParserException {
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(name, text)
				.compile(resolvedTypes, codeMap);
	}
	
	private CompileModule makeCompileModule(
	        List<CompileFile> files,
	        Map<String, FileSpec> fileSpecMap,
	        Map<FileSpec, String> fileToName) throws IOException {
	    
        final List<File> f = new ArrayList<>(files.size());
        
        for (CompileFile compileFile : files) {

            final File file = File.createTempFile("sourcefile", "test");

            file.deleteOnExit();

            IOUtils.write(file, compileFile.getText());
            
            f.add(file);
            
            final FileSystemFileSpec fileSpec = new FileSystemFileSpec(file);
            final String fileName = compileFile.getName();
            
            fileSpecMap.put(fileName, fileSpec);
            fileToName.put(fileSpec, fileName);
        }
        
        final File directory = f.get(0).getParentFile();

        final ModuleId moduleId = new ModuleId("test-module");

        final ProjectModuleResourcePath projectModuleResourcePath
            = new ProjectModuleResourcePath(
                    Arrays.asList(
                            new ModuleResource(moduleId, directory)));
        
        final SourceFolderResource sourceFolderResource
            = new SourceFolderResource(directory, directory.getName(), Language.JAVA);
        
        final SourceFileHolderResourcePath sourceHolderResourcePath
            = new SourceFolderResourcePath(
                    projectModuleResourcePath,
                    sourceFolderResource);
        
        final CompileModule compileModule = new CompileModule(
                projectModuleResourcePath,
                f.stream()
                    .map(path -> new SourceFileResourcePath(
                            sourceHolderResourcePath,
                            new SourceFileResource(path)))
                    .collect(Collectors.toList()),
                Charset.defaultCharset(),
                Collections.emptyList(),
                Collections.emptyList());
        
        return compileModule;
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
			List<CompileFile> files,
			IntCompilerCodeMap codeMap) throws IOException, ParserException {
	    
	    Objects.requireNonNull(files);
	    Objects.requireNonNull(codeMap);
	    
	    final Map<String, FileSpec> fileSpecMap = new HashMap<>(files.size());
	    final Map<FileSpec, String> fileToName = new HashMap<>(files.size());
	    
	    final CompileModule toCompile = makeCompileModule(files, fileSpecMap, fileToName);
	    
	    final CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> compilerLanguage
	        = createCompilerLanguage(codeMap::getTypeNoByTypeName);

        ModulesBuilder.addBuiltinTypesToCodeMap(
                compilerLanguage.getLanguageSpec(),
                codeMap);

		final ModulesBuilder<COMPILATION_UNIT, PARSED_FILE> compiler
				= new ModulesBuilder<>(
				        compilerLanguage.getLanguageSpec(),
				        compilerLanguage.getModel(),
				        new CompilerOptions(true));
		
		final ResolvedModule<PARSED_FILE, ResolveError> resolved
		    = compiler.compile(toCompile, codeMap);
		
		if (resolved == null) {
		    throw new IllegalStateException();
		}
		
		final Map<String, Integer> sourceFileNos = new HashMap<>();
		
		final List<ParsedWithCachedRefs<PARSED_FILE, ResolveError>> moduleParsed
		    = resolved.getParsedModule().getParsed();

		final List<PARSED_FILE> list = new ArrayList<>(moduleParsed.size());

		for (ParsedWithCachedRefs<PARSED_FILE, ResolveError> p : moduleParsed) {

		    list.add(p.getParsedFile());
		    
		    final String fileName = fileToName.get(p.getParsedFile().getFileSpec());

		    if (fileName == null) {
		        throw new IllegalStateException();
		    }

		    sourceFileNos.put(fileName, p.getCodeMapFileNo());
		}
		
		final ParsedFiles<PARSED_FILE> parsedFiles = new ParsedFiles<>(list);
		
		final MappedFiles<PARSED_FILE, COMPILATION_UNIT> mappedFiles
		    = new MappedFiles<PARSED_FILE, COMPILATION_UNIT>(parsedFiles, sourceFileNos) {

            @Override
            public Integer getTypeNo(TypeName typeName) {
                
                Objects.requireNonNull(typeName);
                
                return codeMap.getTypeNoByTypeName(typeName);
            }

            @Override
            public FieldInfo getFieldInfo(TypeName typeName, String fieldName) {

                final Integer typeNo = codeMap.getTypeNoByTypeName(typeName);
                
                if (typeNo == null) {
                    throw new IllegalStateException("No type for " + typeName);
                }
                
                return codeMap.getFieldInfo(typeNo, fieldName);
            }

            @Override
            public CompiledAndResolvedFile getFile(String name) {
                
                final FileSpec fileSystemFileSpec = fileSpecMap.get(name);
                
                return new CompiledAndResolvedFile() {
                    
                    @Override
                    public ParsedFile getParsedFile() {
                        return parsedFiles.getParsedFile(fileSystemFileSpec);
                    }
                    
                    @Override
                    public List<CompileError> getErrors() {
                        
                        final List<CompileError> parseErrors
                            = parsedFiles.getParsedFile(fileSystemFileSpec).getErrors();
                        
                        final List<ResolveError> resolveErrors = moduleParsed.stream()
                                .filter(p -> p.getParsedFile().getFileSpec().equals(fileSystemFileSpec))
                                .findFirst()
                                .get()
                                .getResolveErrorsList();
                        
                        final List<CompileError> errors = new ArrayList<>(parseErrors.size() + resolveErrors.size());

                        errors.addAll(parseErrors);
                        errors.addAll(resolveErrors);
                        
                        return errors;
                    }
                    
                    @Override
                    public <AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type) {
             
                        Objects.requireNonNull(type);
                        
                        return parsedFiles.getParsedFile(fileSystemFileSpec).getASTElements(type);
                    }
                };
            }
        };
        
        return mappedFiles;
	}

	@SuppressWarnings("unchecked")
	protected static <T extends BaseASTElement> T get(List<BaseASTElement> elements, int index) {
		return (T)elements.get(index);
	}

	@SuppressWarnings("unchecked")
	protected static <T extends BaseASTElement> T get(Iterator<BaseASTElement> iterator) {
		return (T)iterator.next();
	}

	@SuppressWarnings("unchecked")
	protected static <T extends BaseASTElement> T getNext(Iterator<BaseASTElement> iterator, Class<T> elementType) {

		while (iterator.hasNext()) {
			
			final BaseASTElement next = iterator.next();
			
			if (next.getClass().equals(elementType)) {
				return (T)next;
			}
		}
		
		return null;
	}
}
