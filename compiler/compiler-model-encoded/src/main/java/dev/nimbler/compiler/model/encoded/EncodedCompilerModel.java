package dev.nimbler.compiler.model.encoded;

import java.util.List;

import com.neaterbits.util.io.strings.Tokenizer;

import dev.nimbler.compiler.ast.encoded.EncodedCompilationUnit;
import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.model.common.ImportsModel;
import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.model.common.ResolveTypesModel;
import dev.nimbler.compiler.model.common.passes.CompilerModel;
import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.parse.CompileError;

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

    @Override
    public IterativeParseTreeListener<EncodedCompilationUnit> createParserListener(String file, Tokenizer tokenizer) {
        // TODO Auto-generated method stub
        return null;
    }
}
