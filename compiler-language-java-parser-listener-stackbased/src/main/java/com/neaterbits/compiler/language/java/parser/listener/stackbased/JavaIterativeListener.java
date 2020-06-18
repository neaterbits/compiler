package com.neaterbits.compiler.language.java.parser.listener.stackbased;

import com.neaterbits.compiler.ast.objects.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.parser.listener.stackbased.ParseTreeFactory;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

@SuppressWarnings("rawtypes")
public class JavaIterativeListener extends BaseIterativeOOParserListener {

    public JavaIterativeListener(
            StringSource stringSource,
            ContextAccess contextAccess,
            ParseLogger logger,
            ParseTreeFactory parseTreeFactory) {
        
        super(stringSource, contextAccess, logger, parseTreeFactory);
    }
}
