package dev.nimbler.compiler.resolver.build;

import java.util.Objects;

import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.model.common.passes.CompilerModel;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.compiler.util.parse.Parser;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypeVariant;

public abstract class BaseBuilder<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> {

    private final Parser<COMPILATION_UNIT> parser;
    private final CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel;
    private final CompilerOptions options;

    BaseBuilder(
            LanguageSpec languageSpec,
            CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel,
            CompilerOptions options) {
        
        Objects.requireNonNull(compilerModel);
        Objects.requireNonNull(options);
        
        this.parser = languageSpec.createParser(compilerModel);

        this.compilerModel = compilerModel;
        this.options = options;
    }

    final Parser<COMPILATION_UNIT> getParser() {
        return parser;
    }

    final CompilerModel<COMPILATION_UNIT, PARSED_FILE> getCompilerModel() {
        return compilerModel;
    }

    final CompilerOptions getOptions() {
        return options;
    }

    public static void addBuiltinTypesToCodeMap(LanguageSpec languageSpec, CompilerCodeMap codeMap) {
        
        for (TypeName builtinTypeName : languageSpec.getBuiltinTypes()) {

            final int typeNo = codeMap.addType(TypeVariant.BUILTIN);

            codeMap.addTypeMapping(builtinTypeName, typeNo);
        }
    }
}
