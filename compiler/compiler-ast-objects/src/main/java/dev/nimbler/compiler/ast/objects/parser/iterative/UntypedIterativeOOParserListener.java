package dev.nimbler.compiler.ast.objects.parser.iterative;

import com.neaterbits.util.io.strings.StringSource;

import dev.nimbler.compiler.parser.listener.common.ContextAccess;
import dev.nimbler.compiler.parser.listener.stackbased.ParseTreeFactory;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
