package com.neaterbits.compiler.resolver;

import java.util.List;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.FieldModifiers;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.CompilerBuilder;
import com.neaterbits.compiler.util.passes.CompilerBuilderIntermediate;
import com.neaterbits.compiler.util.passes.CompilerModel;
import com.neaterbits.compiler.util.passes.FileParsePass;
import com.neaterbits.compiler.util.passes.FileParsePassInput;
import com.neaterbits.compiler.util.passes.LanguageCompiler;

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
