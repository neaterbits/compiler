package com.neaterbits.compiler.ast.objects.parser.iterative;

import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.parser.listener.stackbased.ParseTreeFactory;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

@SuppressWarnings("rawtypes")
public class UntypedIterativeOOParserListener extends BaseIterativeOOParserListener {

    public UntypedIterativeOOParserListener(
            StringSource stringSource,
            ContextAccess contextAccess,
            FullContextProvider fullContextProvider,
            ParseLogger logger,
            ParseTreeFactory parseTreeFactory) {
        
        super(stringSource, contextAccess, fullContextProvider, logger, parseTreeFactory);
    }
}
