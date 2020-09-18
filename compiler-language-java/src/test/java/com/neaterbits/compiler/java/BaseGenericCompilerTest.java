package com.neaterbits.compiler.java;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.model.common.CompiledAndResolvedFile;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.passes.FileParsePassInput;
import com.neaterbits.compiler.model.common.passes.FilePassInput;
import com.neaterbits.compiler.model.common.passes.LanguageCompiler;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseGenericCompilerTest<
            COMPILATION_UNIT,
            PARSED_FILE extends ParsedFile> {
    
    protected abstract Parser<COMPILATION_UNIT> createParser();
    
    protected abstract CompilerLanguage<COMPILATION_UNIT, PARSED_FILE, CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> 
        createCompilerLanguage(Parser<COMPILATION_UNIT> parser);

	protected final CompiledAndResolvedFile compile(String file, String text, ResolvedTypes resolvedTypes) throws IOException, ParserException {
		
		Objects.requireNonNull(file);

		final NameFileSpec fileSpec = new NameFileSpec(file);

		return compile(fileSpec, text, resolvedTypes);
	}

	protected final CompiledAndResolvedFile compile(FileSpec fileSpec, String text, ResolvedTypes resolvedTypes) throws IOException, ParserException {
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(fileSpec, text)
				.compile(resolvedTypes)
				.getFile(fileSpec);
		
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileAndMap(FileSpec fileSpec, String text, ResolvedTypes resolvedTypes) throws IOException, ParserException {
	
		return compileAndMap(fileSpec, text, resolvedTypes, new IntCompilerCodeMap());
	}
	
	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileAndMap(
			FileSpec fileSpec,
			String text,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException, ParserException {
		
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(fileSpec, text)
				.compile(resolvedTypes, codeMap);
		
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
			List<FilePassInput> toCompile,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException, ParserException {
	    
	    final Parser<COMPILATION_UNIT> parser = createParser();

	    final CompilerLanguage<COMPILATION_UNIT, PARSED_FILE, CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compilerLanguage = createCompilerLanguage(parser);
	    
		final LanguageCompiler<FileParsePassInput<COMPILATION_UNIT>, CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compiler
				= compilerLanguage.makeCompilerPasses(resolvedTypes, codeMap);
	
		final List<FileParsePassInput<COMPILATION_UNIT>> parseInputs = toCompile.stream()
				.map(input -> new FileParsePassInput<>(
				                            input.getInputStream(),
				                            input.getCharset(),
				                            input.getFile(),
				                            parser))
				.collect(Collectors.toList());
		
		return compiler.compile(parseInputs);
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
