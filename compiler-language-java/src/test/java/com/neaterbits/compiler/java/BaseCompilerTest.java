package com.neaterbits.compiler.java;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.model.CompiledAndMappedFiles;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFile;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.passes.FileParsePassInput;
import com.neaterbits.compiler.util.passes.FilePassInput;
import com.neaterbits.compiler.util.passes.LanguageCompiler;

public abstract class BaseCompilerTest {

	protected final CompiledAndResolvedFile compile(String file, String text, ResolvedTypes resolvedTypes) throws IOException {
		
		Objects.requireNonNull(file);

		final NameFileSpec fileSpec = new NameFileSpec(file);

		return compile(fileSpec, text, resolvedTypes);
	}

	protected final CompiledAndResolvedFile compile(FileSpec fileSpec, String text, ResolvedTypes resolvedTypes) throws IOException {
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector()
				.add(fileSpec, text)
				.compile(resolvedTypes)
				.getFile(fileSpec);
		
	}

	protected final CompiledAndMappedFiles compileAndMap(FileSpec fileSpec, String text, ResolvedTypes resolvedTypes) throws IOException {
	
		return compileAndMap(fileSpec, text, resolvedTypes, new IntCompilerCodeMap());
	}
	
	protected final CodeMapCompiledAndMappedFiles<CompilationUnit> compileAndMap(
			FileSpec fileSpec,
			String text,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException {
		
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector()
				.add(fileSpec, text)
				.compile(resolvedTypes, codeMap);
		
	}

	protected static CodeMapCompiledAndMappedFiles<CompilationUnit> compile(
			List<FilePassInput> toCompile,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException {
		
		final JavaAntlrCompilerLanguage compilerLanguage = new JavaAntlrCompilerLanguage();
		
		final LanguageCompiler<FileParsePassInput<CompilationUnit>, CodeMapCompiledAndMappedFiles<CompilationUnit>> compiler
				= compilerLanguage.makeCompilerPasses(resolvedTypes, codeMap);
	
		final List<FileParsePassInput<CompilationUnit>> parseInputs = toCompile.stream()
				.map(input -> new FileParsePassInput<>(input.getInputStream(), input.getCharset(), input.getFile(), compilerLanguage.getParser()))
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
