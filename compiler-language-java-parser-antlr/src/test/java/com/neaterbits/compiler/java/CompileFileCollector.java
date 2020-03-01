package com.neaterbits.compiler.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.model.CompiledAndMappedFiles;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.passes.FilePassInput;

public class CompileFileCollector {

	private static class CompileFile {
		private final FileSpec name;
		private final String text;
		
		CompileFile(FileSpec name, String text) {

			this.name = name;
			this.text = text;
		}
	}
	
	private final List<CompileFile> files;

	public CompileFileCollector() {
		this.files = new ArrayList<>();
	}
	
	public CompileFileCollector add(FileSpec name, String text) {

		files.add(new CompileFile(name, text));
		
		return this;
	}

	public CompileFileCollector add(String name, String text) {
		return add(new NameFileSpec(name), text);
	}

	public CompiledAndMappedFiles compile(ResolvedTypes resolvedTypes) throws IOException {
		return compile(resolvedTypes, new IntCompilerCodeMap());
	}

	public CodeMapCompiledAndMappedFiles<CompilationUnit>
		compile(ResolvedTypes resolvedTypes, CompilerCodeMap codeMap) throws IOException {
		
		final List<FilePassInput> parseInputs = files.stream()
				
				.map(compileFile -> new FilePassInput(
						new ByteArrayInputStream(compileFile.text.getBytes()),
						Charset.defaultCharset(),
						compileFile.name))

				.collect(Collectors.toList());

		return BaseCompilerTest.compile(parseInputs, resolvedTypes, codeMap);
	}
}
