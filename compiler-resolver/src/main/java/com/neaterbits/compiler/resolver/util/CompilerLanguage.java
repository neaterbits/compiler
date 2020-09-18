package com.neaterbits.compiler.resolver.util;

import java.util.Objects;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.CompiledAndMappedFiles;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.model.common.passes.FileParsePassInput;
import com.neaterbits.compiler.model.common.passes.LanguageCompiler;
import com.neaterbits.compiler.resolver.LanguageSpec;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;

public class CompilerLanguage<
		COMPILATION_UNIT,
		PARSED_FILE extends ParsedFile,
		OUTPUT extends CompiledAndMappedFiles> {
    
    private final LanguageSpec languageSpec;
    private final CompilerModel<COMPILATION_UNIT, PARSED_FILE> model;

	public CompilerLanguage(LanguageSpec languageSpec, CompilerModel<COMPILATION_UNIT, PARSED_FILE> model) {
	    
	    Objects.requireNonNull(languageSpec);
	    Objects.requireNonNull(model);

	    this.languageSpec = languageSpec;
	    this.model = model;
    }

    public final LanguageCompiler<
		FileParsePassInput<COMPILATION_UNIT>, CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> makeCompilerPasses(
		        ResolvedTypes resolvedTypes,
		        CompilerCodeMap codeMap) {
        
        return languageSpec.makeCompilerPasses(model, resolvedTypes, codeMap);
    }
}
