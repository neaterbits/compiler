package dev.nimbler.compiler.resolver.util;

import java.util.Objects;

import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.model.common.passes.CompilerModel;
import dev.nimbler.compiler.util.parse.ParsedFile;

public class CompilerLanguage<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> {
    
    private final LanguageSpec languageSpec;
    private final CompilerModel<COMPILATION_UNIT, PARSED_FILE> model;

	public CompilerLanguage(LanguageSpec languageSpec, CompilerModel<COMPILATION_UNIT, PARSED_FILE> model) {
	    
	    Objects.requireNonNull(languageSpec);
	    Objects.requireNonNull(model);

	    this.languageSpec = languageSpec;
	    this.model = model;
    }

    public final LanguageSpec getLanguageSpec() {
        return languageSpec;
    }

    public final CompilerModel<COMPILATION_UNIT, PARSED_FILE> getModel() {
        return model;
    }
}
