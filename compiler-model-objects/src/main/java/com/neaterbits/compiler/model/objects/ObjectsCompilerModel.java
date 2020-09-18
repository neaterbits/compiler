package com.neaterbits.compiler.model.objects;

import java.util.List;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.ImportsModel;
import com.neaterbits.compiler.model.common.ResolveTypesModel;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;

public final class ObjectsCompilerModel implements CompilerModel<CompilationUnit, ASTParsedFile> {

    private final ObjectProgramModel programModel;
    
    public ObjectsCompilerModel() {

        this.programModel = new ObjectProgramModel();
    }

    @Override
    public ASTParsedFile createParsedFile(
            FileSpec file,
            CompilationUnit parsed,
            List<CompileError> errors,
            String log) {

        return new ASTParsedFile(file, errors, log, parsed);
    }

    @Override
    public FileSpec getFileSpec(ASTParsedFile parsedFile) {

        return parsedFile.getFileSpec();
    }

    @Override
    public CompilationUnitModel<CompilationUnit> getCompilationUnitModel() {

        return programModel;
    }

    @Override
    public ImportsModel<CompilationUnit> getImportsModel() {

        return programModel;
    }

    @Override
    public ResolveTypesModel<CompilationUnit> getResolveTypesModel() {

        return programModel;
    }
}
