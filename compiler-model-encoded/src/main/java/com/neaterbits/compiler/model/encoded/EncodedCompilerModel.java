package com.neaterbits.compiler.model.encoded;

import java.util.List;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.ImportsModel;
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.model.common.ResolveTypesModel;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;

public final class EncodedCompilerModel
    implements CompilerModel<EncodedCompilationUnit, EncodedParsedFile> {
    
    private final EncodedProgramModel programModel;
    
    EncodedCompilerModel(LanguageSpec languageSpec) {
        this.programModel = new EncodedProgramModel(languageSpec.getImplicitImports());
    }

    @Override
    public EncodedParsedFile createParsedFile(
            FileSpec file,
            EncodedCompilationUnit parsed,
            List<CompileError> errors,
            String log) {

        return new EncodedParsedFile(file, errors, log, parsed);
    }

    @Override
    public FileSpec getFileSpec(EncodedParsedFile parsedFile) {
        return parsedFile.getFileSpec();
    }

    @Override
    public CompilationUnitModel<EncodedCompilationUnit> getCompilationUnitModel() {
        return programModel;
    }

    @Override
    public ImportsModel<EncodedCompilationUnit> getImportsModel() {
        return programModel;
    }

    @Override
    public ResolveTypesModel<EncodedCompilationUnit> getResolveTypesModel() {
        // TODO Auto-generated method stub
        return null;
    }
}
