package com.neaterbits.compiler.java;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.strategies.compilemodules.CompileModule;
import com.neaterbits.build.strategies.compilemodules.ResolvedModule;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.resolve.TestResolvedTypes;
import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.LanguageCompiler;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.types.FieldInfo;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseGenericCompilerTest<
            COMPILATION_UNIT,
            PARSED_FILE extends ParsedFile> {
    
    protected abstract Parser<COMPILATION_UNIT> createParser();
    
    protected abstract CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> 
        createCompilerLanguage();

	protected final CompiledAndResolvedFile compile(String file, String text, TestResolvedTypes resolvedTypes) throws IOException, ParserException {
		
		Objects.requireNonNull(file);

		final NameFileSpec fileSpec = new NameFileSpec(file);

		return compile(fileSpec, text, resolvedTypes);
	}

	protected final CompiledAndResolvedFile compile(FileSpec fileSpec, String text, TestResolvedTypes resolvedTypes)
	        throws IOException, ParserException {
	    
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(fileSpec, text)
				.compile(resolvedTypes)
				.getFile(fileSpec);
		
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileAndMap(
			FileSpec fileSpec,
			String text,
			TestResolvedTypes resolvedTypes,
			IntCompilerCodeMap codeMap) throws IOException, ParserException {
		
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(fileSpec, text)
				.compile(resolvedTypes, codeMap);
		
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
			CompileModule toCompile,
			IntCompilerCodeMap codeMap) throws IOException, ParserException {
	    
	    Objects.requireNonNull(toCompile);
	    Objects.requireNonNull(codeMap);
	    
	    final Parser<COMPILATION_UNIT> parser = createParser();

	    final CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> compilerLanguage = createCompilerLanguage();
	    
		final LanguageCompiler<COMPILATION_UNIT, PARSED_FILE> compiler
				= new LanguageCompiler<>(parser, compilerLanguage.getModel());
		
		final ResolvedModule<PARSED_FILE, ResolveError> resolved = compiler.compile(toCompile, codeMap);
		
		if (resolved == null) {
		    throw new IllegalStateException();
		}
		
		final Map<FileSpec, Integer> sourceFileNos = new HashMap<>();

		final ParsedFiles<PARSED_FILE> parsedFiles
		        = new ParsedFiles<>(resolved.getParsedModule().getParsed().stream()
		                .map(p -> p.getParsedFile())
		                .collect(Collectors.toList()));
		
		final MappedFiles<PARSED_FILE, COMPILATION_UNIT> mappedFiles
		    = new MappedFiles<PARSED_FILE, COMPILATION_UNIT>(parsedFiles, sourceFileNos) {

            @Override
            public Integer getTypeNo(TypeName typeName) {
                
                Objects.requireNonNull(typeName);
                
                System.out.println("## get type " + typeName + " from " + codeMap);
                
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
            public CompiledAndResolvedFile getFile(FileSpec fileSpec) {
                
                return new CompiledAndResolvedFile() {
                    
                    @Override
                    public ParsedFile getParsedFile() {
                        return parsedFiles.getParsedFile(fileSpec);
                    }
                    
                    @Override
                    public List<CompileError> getErrors() {

                        return parsedFiles.getParsedFile(fileSpec).getErrors();
                    }
                    
                    @Override
                    public <AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type) {
                        
                        return parsedFiles.getParsedFile(fileSpec).getASTElements(type);
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
