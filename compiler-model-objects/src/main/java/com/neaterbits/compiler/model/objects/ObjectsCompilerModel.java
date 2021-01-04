package com.neaterbits.compiler.model.objects;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.objects.parser.iterative.UntypedIterativeOOParserListener;
import com.neaterbits.compiler.ast.objects.type.primitive.BuiltinType;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.ImportsModel;
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.model.common.ResolveTypesModel;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.parser.listener.common.CreateParserListener;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.parser.listener.common.ListContextAccess;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.StringSourceFullContextProvider;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.io.strings.Tokenizer;

public final class ObjectsCompilerModel implements CompilerModel<CompilationUnit, ASTParsedFile> {

    private final Collection<BuiltinType> builtinTypes;
    private final GetBuiltinTypeNo getBuiltinTypeNo;

    private final ObjectProgramModel programModel;

    public ObjectsCompilerModel(
            LanguageSpec languageSpec,
            Collection<BuiltinType> builtinTypes,
            GetBuiltinTypeNo getBuiltinTypeNo) {
        
        Objects.requireNonNull(languageSpec);

        this.builtinTypes = builtinTypes;
        this.getBuiltinTypeNo = getBuiltinTypeNo;

        this.programModel = new ObjectProgramModel(
                languageSpec.getImplicitImports(),
                languageSpec.getDefaultModifiers());
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

    @Override
    public IterativeParseTreeListener<CompilationUnit> createParserListener(String file, Tokenizer tokenizer) {
        
        return createListener(builtinTypes, getBuiltinTypeNo).createParserListener(file, tokenizer);
    }

    public static CreateParserListener<CompilationUnit>
    createListener(Collection<BuiltinType> builtinTypes, GetBuiltinTypeNo getBuiltinTypeNo) {

        final ASTParseTreeFactory parseTreeFactory
            = new ASTParseTreeFactory(builtinTypes, getBuiltinTypeNo);
        
        return (file, stringBuffers) -> makeListener(file, stringBuffers, parseTreeFactory);
    }
    
    private static IterativeParseTreeListener<CompilationUnit> makeListener(
            String file,
            StringSource stringSource,
            ASTParseTreeFactory parseTreeFactory) {

        final FullContextProvider fullContextProvider = new StringSourceFullContextProvider(file, stringSource);
        
        final ParseLogger parseLogger = new ParseLogger(System.out, fullContextProvider);

        @SuppressWarnings("unchecked")
        final IterativeParseTreeListener<CompilationUnit> listener = new UntypedIterativeOOParserListener(
                stringSource,
                new ListContextAccess(),
                fullContextProvider,
                parseLogger,
                parseTreeFactory);

        return listener;
    }
}
