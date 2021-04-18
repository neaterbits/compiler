package dev.nimbler.compiler.javascript.parser;

import org.jutils.io.strings.StringSource;

import dev.nimbler.compiler.antlr4.ModelParserListener;
import dev.nimbler.compiler.ast.objects.parser.iterative.BaseIterativeOOParserListener;
import dev.nimbler.compiler.javascript.ast.JavascriptProgram;
import dev.nimbler.compiler.parser.listener.common.ContextAccess;
import dev.nimbler.compiler.parser.listener.stackbased.ParseTreeFactory;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.parse.ParseLogger;

@SuppressWarnings("rawtypes")
public class JSParserListener extends BaseIterativeOOParserListener
			implements ModelParserListener<JavascriptProgram> {

	public JSParserListener(
	        StringSource stringSource,
	        ContextAccess contextAccess,
            FullContextProvider fullContextProvider,
	        ParseLogger logger,
	        ParseTreeFactory parseTreeFactory) {
		super(stringSource, contextAccess, fullContextProvider, logger, parseTreeFactory);
	}

	@Override
	public JavascriptProgram getResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
