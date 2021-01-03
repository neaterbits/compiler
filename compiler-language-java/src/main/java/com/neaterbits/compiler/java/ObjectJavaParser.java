package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.parser.java.JavaParser;
import com.neaterbits.compiler.parser.listener.common.CreateParserListener;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.parser.listener.common.ListContextAccess;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.StringSourceFullContextProvider;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

public final class ObjectJavaParser extends JavaParser<CompilationUnit> {

    public ObjectJavaParser() {
        this(null);
    }

    public ObjectJavaParser(GetBuiltinTypeNo getBuiltinTypeNo) {
        super(createListener(getBuiltinTypeNo));
    }

    public static CreateParserListener<CompilationUnit>
    createListener(GetBuiltinTypeNo getBuiltinTypeNo) {

        final ASTParseTreeFactory parseTreeFactory
            = new ASTParseTreeFactory(JavaTypes.getBuiltinTypes(), getBuiltinTypeNo);
        
        return (file, stringBuffers) -> makeListener(file, stringBuffers, parseTreeFactory);
    }
    
    private static IterativeParseTreeListener<CompilationUnit> makeListener(
            String file,
            StringSource stringSource,
            ASTParseTreeFactory parseTreeFactory) {

        final FullContextProvider fullContextProvider = new StringSourceFullContextProvider(file, stringSource);
        
        final ParseLogger parseLogger = new ParseLogger(System.out, fullContextProvider);

        @SuppressWarnings("unchecked")
        final IterativeParseTreeListener<CompilationUnit> listener = new JavaIterativeListener(
                stringSource,
                new ListContextAccess(),
                fullContextProvider,
                parseLogger,
                parseTreeFactory);

        return listener;
    }
}
