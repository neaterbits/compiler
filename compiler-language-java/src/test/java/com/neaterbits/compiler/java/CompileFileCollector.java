package com.neaterbits.compiler.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.model.common.CompiledAndMappedFiles;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.passes.FilePassInput;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.util.parse.ParserException;

public class CompileFileCollector<COMPILATION_UNIT> {
    
    @FunctionalInterface
    public interface Compiler<COMPILED_AND_MAPPED_FILES> {
        
        COMPILED_AND_MAPPED_FILES compile(
                    List<FilePassInput> parseInputs,
                    ResolvedTypes resolvedTypes,
                    CompilerCodeMap codeMap) throws IOException, ParserException;
    }

	private static class CompileFile {
		private final FileSpec name;
		private final String text;
		
		CompileFile(FileSpec name, String text) {

			this.name = name;
			this.text = text;
		}
	}
	
	private final Compiler<CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compiler;
	
	private final List<CompileFile> files;

	public CompileFileCollector(Compiler<CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compiler) {
	    
	    Objects.requireNonNull(compiler);
	    
	    this.compiler = compiler;
	    
		this.files = new ArrayList<>();
	}
	
	public CompileFileCollector<COMPILATION_UNIT> add(FileSpec name, String text) {

		files.add(new CompileFile(name, text));
		
		return this;
	}

	public CompileFileCollector<COMPILATION_UNIT> add(String name, String text) {

		return add(new NameFileSpec(name), text);
	}

	public CompiledAndMappedFiles compile(ResolvedTypes resolvedTypes) throws IOException, ParserException {
		return compile(resolvedTypes, new IntCompilerCodeMap());
	}

	public CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>
		compile(ResolvedTypes resolvedTypes, CompilerCodeMap codeMap) throws IOException, ParserException {
		
		final List<FilePassInput> parseInputs = files.stream()
				
				.map(compileFile -> new FilePassInput(
						new ByteArrayInputStream(compileFile.text.getBytes()),
						Charset.defaultCharset(),
						compileFile.name))

				.collect(Collectors.toList());

		return compiler.compile(parseInputs, resolvedTypes, codeMap);
	}
}
