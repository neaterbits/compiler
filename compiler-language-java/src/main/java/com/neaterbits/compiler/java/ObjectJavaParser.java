package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.parser.java.JavaParser;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.parser.listener.common.ListContextAccess;
import com.neaterbits.compiler.util.CastFullContextProvider;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.buffers.StringBuffers;
import com.neaterbits.util.io.strings.StringSource;

public final class ObjectJavaParser extends JavaParser<CompilationUnit> {

    public ObjectJavaParser() {
        this(
                new ASTParseTreeFactory(JavaTypes.getBuiltinTypes()),
                new ParseLogger(System.out, CastFullContextProvider.INSTANCE));
    }
    
    public ObjectJavaParser(ASTParseTreeFactory parseTreeFactory, ParseLogger logger) {
        super(stringBuffers -> makeListener(stringBuffers, parseTreeFactory, logger));
    }

    private static IterativeParseTreeListener<CompilationUnit> makeListener(
            StringBuffers stringBuffers,
            ASTParseTreeFactory parseTreeFactory,
            ParseLogger logger) {
        
        final StringSource stringSource = new StringSource() {
            
            @Override
            public String asString(long stringRef) {
                return stringBuffers.getString(stringRef);
            }
        };
          
        @SuppressWarnings("unchecked")
        final IterativeParseTreeListener<CompilationUnit> listener = new JavaIterativeListener(
                stringSource,
                new ListContextAccess(),
                logger,
                parseTreeFactory);
        
        return listener;
    }
}
