package com.neaterbits.compiler.parser.java;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaTypes;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.listener.common.ListContextAccess;
import com.neaterbits.compiler.util.CastFullContextProvider;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;
import com.test.util.io.buffers.StringBuffers;

final class ObjectJavaParser extends JavaParser<CompilationUnit> {

    public ObjectJavaParser() {
        this(
                new ASTParseTreeFactory(JavaTypes.getBuiltinTypes()),
                new ParseLogger(System.out, CastFullContextProvider.INSTANCE));
    }
    
    public ObjectJavaParser(ASTParseTreeFactory parseTreeFactory, ParseLogger logger) {
        super(stringBuffers -> makeListener(stringBuffers, parseTreeFactory, logger));
    }

    private static IterativeParserListener<CompilationUnit> makeListener(
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
        final IterativeParserListener<CompilationUnit> listener = new JavaIterativeListener(
                stringSource,
                new ListContextAccess(),
                logger,
                parseTreeFactory);
        
        return listener;
    }
}
