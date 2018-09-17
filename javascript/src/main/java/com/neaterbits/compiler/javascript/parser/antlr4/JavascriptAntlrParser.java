package com.neaterbits.compiler.javascript.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

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

	private final ParseLogger logger;
	
	public JavascriptAntlrParser(boolean debug) {
		super(debug, new JavaScriptLexer(null), new JavaScriptParser(null));
		
		this.logger = new ParseLogger();
	}

	@Override
	protected JSParserListener createListener() {
		return new JSParserListener(logger);
	}

	@Override
	protected ParserRuleContext getMainContext(JavaScriptParser parser) {
		return parser.program();
	}

	@Override
	protected ParseTreeListener makeParseTreeListener(JSParserListener listener) {
		return new JavascriptAntlrParserListener(listener);
	}
}
