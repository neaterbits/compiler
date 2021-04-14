package dev.nimbler.compiler.language.java.parser.listener.stackbased;

import com.neaterbits.util.io.strings.StringSource;

import dev.nimbler.compiler.ast.objects.parser.iterative.UntypedIterativeOOParserListener;
import dev.nimbler.compiler.parser.listener.common.ContextAccess;
import dev.nimbler.compiler.parser.listener.stackbased.ParseTreeFactory;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.parse.ParseLogger;

@SuppressWarnings("rawtypes")
public class JavaIterativeListener extends UntypedIterativeOOParserListener {

    public JavaIterativeListener(
            StringSource stringSource,
            ContextAccess contextAccess,
            FullContextProvider fullContextProvider,
            ParseLogger logger,
            ParseTreeFactory parseTreeFactory) {
        
        super(stringSource, contextAccess, fullContextProvider, logger, parseTreeFactory);
    }
}
