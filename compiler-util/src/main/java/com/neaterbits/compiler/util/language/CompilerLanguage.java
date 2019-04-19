package com.neaterbits.compiler.util.language;

import java.util.function.Function;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.CompiledAndMappedFiles;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.compiler.util.passes.CompilerBuilder;
import com.neaterbits.compiler.util.passes.CompilerBuilderIntermediate;
import com.neaterbits.compiler.util.passes.CreateParsedFile;
import com.neaterbits.compiler.util.passes.FileParsePass;
import com.neaterbits.compiler.util.passes.FileParsePassInput;
import com.neaterbits.compiler.util.passes.LanguageCompiler;

public abstract class CompilerLanguage<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> {

	public abstract LanguageCompiler<
		FileParsePassInput<COMPILATION_UNIT>,
		CompiledAndMappedFiles> makeCompilerPasses(ResolvedTypes resolvedTypes);
	
	protected abstract Parser<COMPILATION_UNIT> getParser();  

	protected abstract CompilerBuilderIntermediate<PARSED_FILE, FileParsePassInput<COMPILATION_UNIT>> buildCompilerParsePass();
			
	protected final CompilerBuilderIntermediate<PARSED_FILE, FileParsePassInput<COMPILATION_UNIT>> 
	
		buildCompilerParsePass(
			CreateParsedFile<COMPILATION_UNIT, PARSED_FILE> makeParsedFile,
			Function<PARSED_FILE, FileSpec> getFileSpec) {
		
		final CompilerBuilder<FileParsePassInput<COMPILATION_UNIT>> builder = new CompilerBuilder<>();
		
		final FileParsePass<COMPILATION_UNIT, PARSED_FILE> parsePass = new FileParsePass<COMPILATION_UNIT, PARSED_FILE>(
				makeParsedFile,
				getFileSpec);
		
		return builder.addPass(parsePass);
	}
}
