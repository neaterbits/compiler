package com.neaterbits.compiler.util.passes;

import java.util.List;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.CompilationUnitModel;
import com.neaterbits.compiler.util.model.ImportsModel;
import com.neaterbits.compiler.util.model.ResolveTypesModel;
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
