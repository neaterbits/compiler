package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.parser.listener.common.ListContextAccess;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

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

        final JavaIterativeListener delegate = new JavaIterativeListener(
                stringSource,
                new ListContextAccess(),
                parseLogger,
                parseTreeFactory);

        return new JavaParserListener<>(
                stringSource,
                parseLogger,
                file,
                delegate);
    }
}
