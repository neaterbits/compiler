package dev.nimbler.compiler.javascript.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.jutils.io.strings.StringSource;

import dev.nimbler.compiler.javascript.JavaScriptLexer;
import dev.nimbler.compiler.javascript.JavaScriptParser;
import dev.nimbler.compiler.antlr4.AntlrParserFactory;
import dev.nimbler.compiler.antlr4.BaseAntlrParser;
import dev.nimbler.compiler.javascript.ast.JavascriptProgram;
import dev.nimbler.compiler.javascript.parser.JSParserListener;
import dev.nimbler.compiler.parser.listener.common.ListContextAccess;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class JavascriptAntlrParser extends BaseAntlrParser<
			JavascriptProgram,
			JSParserListener,
			JavaScriptLexer,
			JavaScriptParser> {

	public JavascriptAntlrParser(boolean debug) {
		super(debug, new AntlrParserFactory<>(JavaScriptLexer::new, JavaScriptParser::new));
	}

	@Override
	protected JSParserListener createListener(StringSource stringSource, ParseLogger logger, String file) {
		return new JSParserListener(stringSource, new ListContextAccess(), null, logger, null);
	}

	@Override
	protected ParserRuleContext getMainContext(JavaScriptParser parser) {
		return parser.program();
	}

	@Override
	protected ParseTreeListener makeParseTreeListener(JSParserListener listener, boolean debug, String file, ParseLogger logger) {
		return new JavascriptAntlrParserListener(listener, debug, logger);
	}
}
