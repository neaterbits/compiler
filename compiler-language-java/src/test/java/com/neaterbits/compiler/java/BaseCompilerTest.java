package com.neaterbits.compiler.java;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFile;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFiles;
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

		
	protected static CompiledAndResolvedFiles compile(List<FilePassInput> toCompile, ResolvedTypes resolvedTypes) throws IOException {
		
		final JavaAntlrCompilerLanguage compilerLanguage = new JavaAntlrCompilerLanguage();
		
		final LanguageCompiler<FileParsePassInput<CompilationUnit>, CompiledAndResolvedFiles> compiler
				= compilerLanguage.makeCompilerPasses(resolvedTypes);
	
		final List<FileParsePassInput<CompilationUnit>> parseInputs = toCompile.stream()
				.map(input -> new FileParsePassInput<>(input.getInputStream(), input.getFile(), compilerLanguage.getParser()))
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
}
