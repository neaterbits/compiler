package com.neaterbits.compiler.resolver;

import java.util.List;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.passes.CompilerBuilder;
import com.neaterbits.compiler.model.common.passes.CompilerBuilderIntermediate;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.model.common.passes.FileParsePass;
import com.neaterbits.compiler.model.common.passes.FileParsePassInput;
import com.neaterbits.compiler.model.common.passes.LanguageCompiler;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.types.FieldModifiers;
import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.parse.ParsedFile;

public interface LanguageSpec {

    List<TypeImport> getImplicitImports();
    
    FieldModifiers getDefaultModifiers();

    <COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
        CompilerBuilderIntermediate<PARSED_FILE, FileParsePassInput<COMPILATION_UNIT>>
            buildCompilerParsePass(CompilerModel<COMPILATION_UNIT, PARSED_FILE> model);

    <COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
        LanguageCompiler<FileParsePassInput<COMPILATION_UNIT>, CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>>
            makeCompilerPasses(
                    CompilerModel<COMPILATION_UNIT, PARSED_FILE> model,
                    ResolvedTypes resolvedTypes,
                    CompilerCodeMap codeMap);

    default <COMPILATION_UNIT, PARSED_FILE extends ParsedFile> CompilerBuilderIntermediate<PARSED_FILE, FileParsePassInput<COMPILATION_UNIT>> 
    
        buildCompilerParsePass(CompilerModel<COMPILATION_UNIT, PARSED_FILE> model, FullContextProvider fullContextProvider) {
        
        final CompilerBuilder<FileParsePassInput<COMPILATION_UNIT>> builder = new CompilerBuilder<>();
        
        final FileParsePass<COMPILATION_UNIT, PARSED_FILE> parsePass
            = new FileParsePass<COMPILATION_UNIT, PARSED_FILE>(model,fullContextProvider);
        
        return builder.addPass(parsePass);
    }
}
