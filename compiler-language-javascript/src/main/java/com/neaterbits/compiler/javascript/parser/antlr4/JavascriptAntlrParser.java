package com.neaterbits.compiler.javascript.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import com.neaterbits.compiler.antlr4.AntlrParserFactory;
import com.neaterbits.compiler.antlr4.BaseAntlrParser;
import com.neaterbits.compiler.javascript.JavaScriptLexer;
import com.neaterbits.compiler.javascript.JavaScriptParser;
import com.neaterbits.compiler.javascript.ast.JavascriptProgram;
import com.neaterbits.compiler.javascript.parser.JSParserListener;
import com.neaterbits.compiler.util.TokenSequenceNoGenerator;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

public class JavascriptAntlrParser extends BaseAntlrParser<
			JavascriptProgram,
			JSParserListener,
			JavaScriptLexer,
			JavaScriptParser> {

	public JavascriptAntlrParser(boolean debug) {
		super(debug, new AntlrParserFactory<>(JavaScriptLexer::new, JavaScriptParser::new));
	}

	@Override
	protected JSParserListener createListener(StringSource stringSource, ParseLogger logger, String file, TokenSequenceNoGenerator gen) {
		return new JSParserListener(stringSource, logger, null);
	}

	@Override
	protected ParserRuleContext getMainContext(JavaScriptParser parser) {
		return parser.program();
	}

	@Override
	protected ParseTreeListener makeParseTreeListener(JSParserListener listener, boolean debug, String file, ParseLogger logger, TokenSequenceNoGenerator gen) {
		return new JavascriptAntlrParserListener(listener, debug, logger);
	}
}
