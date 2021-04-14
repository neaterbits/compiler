package dev.nimbler.compiler.model.common.passes;

import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.model.common.ImportsModel;
import dev.nimbler.compiler.model.common.ParsedFileCreator;
import dev.nimbler.compiler.model.common.ResolveTypesModel;
import dev.nimbler.compiler.parser.listener.common.CreateParserListener;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.parse.ParsedFile;

public interface CompilerModel<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
        extends ParsedFileCreator<PARSED_FILE, COMPILATION_UNIT>, CreateParserListener<COMPILATION_UNIT> {

    FileSpec getFileSpec(PARSED_FILE parsedFile);
    
    CompilationUnitModel<COMPILATION_UNIT> getCompilationUnitModel();

    ImportsModel<COMPILATION_UNIT> getImportsModel();
    
    ResolveTypesModel<COMPILATION_UNIT> getResolveTypesModel();
}
