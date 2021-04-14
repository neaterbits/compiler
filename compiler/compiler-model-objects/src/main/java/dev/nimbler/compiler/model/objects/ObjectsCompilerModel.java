package dev.nimbler.compiler.model.objects;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.io.strings.Tokenizer;

import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import dev.nimbler.compiler.ast.objects.parser.ASTParsedFile;
import dev.nimbler.compiler.ast.objects.parser.iterative.UntypedIterativeOOParserListener;
import dev.nimbler.compiler.ast.objects.type.primitive.BuiltinType;
import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.model.common.ImportsModel;
import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.model.common.ResolveTypesModel;
import dev.nimbler.compiler.model.common.passes.CompilerModel;
import dev.nimbler.compiler.parser.listener.common.CreateParserListener;
import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.listener.common.ListContextAccess;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.StringSourceFullContextProvider;
import dev.nimbler.compiler.util.parse.CompileError;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
