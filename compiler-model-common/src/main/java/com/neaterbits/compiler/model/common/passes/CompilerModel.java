package com.neaterbits.compiler.model.common.passes;

import java.util.List;

import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.ImportsModel;
import com.neaterbits.compiler.model.common.ResolveTypesModel;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;

public interface CompilerModel<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> {

    PARSED_FILE createParsedFile(
            FileSpec file,
            COMPILATION_UNIT parsed,
            List<CompileError> errors,
            String log);

    FileSpec getFileSpec(PARSED_FILE parsedFile);
    
    CompilationUnitModel<COMPILATION_UNIT> getCompilationUnitModel();

    ImportsModel<COMPILATION_UNIT> getImportsModel();
    
    ResolveTypesModel<COMPILATION_UNIT> getResolveTypesModel();
}
