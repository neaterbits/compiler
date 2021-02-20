package com.neaterbits.compiler.javascript.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import com.neaterbits.compiler.common.antlr4.AntlrParserFactory;
import com.neaterbits.compiler.common.antlr4.BaseParser;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.javascript.JavaScriptLexer;
import com.neaterbits.compiler.javascript.JavaScriptParser;
import com.neaterbits.compiler.javascript.ast.JavascriptProgram;
import com.neaterbits.compiler.javascript.parser.JSParserListener;

public class JavascriptAntlrParser extends BaseParser<
			JavascriptProgram,
			JSParserListener,
			JavaScriptLexer,
			JavaScriptParser> {

	public JavascriptAntlrParser(boolean debug) {
		super(debug, new AntlrParserFactory<>(JavaScriptLexer::new, JavaScriptParser::new));
	}

	@Override
	protected JSParserListener createListener(ParseLogger logger) {
		return new JSParserListener(logger);
	}

	@Override
	protected ParserRuleContext getMainContext(JavaScriptParser parser) {
		return parser.program();
	}

	@Override
	protected ParseTreeListener makeParseTreeListener(JSParserListener listener, boolean debug, ParseLogger logger) {
		return new JavascriptAntlrParserListener(listener, debug, logger);
	}
}
