package dev.nimbler.compiler.language.java;

import org.jutils.io.strings.StringSource;

import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.language.java.parser.antlr.JavaParserListener;
import dev.nimbler.compiler.language.java.parser.antlr.antlr4.Java8AntlrParser;
import dev.nimbler.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import dev.nimbler.compiler.parser.listener.common.ListContextAccess;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.StringSourceFullContextProvider;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class Java8AntlrObjectModelParser extends Java8AntlrParser<CompilationUnit> {

    public Java8AntlrObjectModelParser(boolean debug) {
        super(debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JavaParserListener<CompilationUnit> createListener(
            StringSource stringSource,
            ParseLogger parseLogger,
            String file) {

        final ASTParseTreeFactory parseTreeFactory = new ASTParseTreeFactory(JavaTypes.getBuiltinTypes(), null);

        final FullContextProvider fullContextProvider = new StringSourceFullContextProvider(file, stringSource);
        
        final JavaIterativeListener delegate = new JavaIterativeListener(
                stringSource,
                new ListContextAccess(),
                fullContextProvider,
                parseLogger,
                parseTreeFactory);

        return new JavaParserListener<>(
                stringSource,
                parseLogger,
                file,
                delegate);
    }
}
