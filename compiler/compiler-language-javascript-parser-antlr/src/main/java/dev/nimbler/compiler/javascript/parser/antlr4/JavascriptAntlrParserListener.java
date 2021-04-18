package dev.nimbler.compiler.javascript.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.jutils.Strings;

import dev.nimbler.compiler.javascript.JavaScriptParser;
import dev.nimbler.compiler.javascript.JavaScriptParserBaseListener;
import dev.nimbler.compiler.javascript.parser.JSParserListener;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class JavascriptAntlrParserListener extends JavaScriptParserBaseListener {

	// private final JSParserListener delegate;

	private final boolean debug;
	private final ParseLogger logger;
	
	private int indent = 0;

	public JavascriptAntlrParserListener(JSParserListener delegate, boolean debug, ParseLogger logger) {
		// this.delegate = delegate;
		
		this.debug = debug;
		this.logger = logger;
	}
	
	
	private boolean isDebugEnabled() {
		return debug;
	}
	
	private String ruleName(ParserRuleContext ctx) {
		return JavaScriptParser.ruleNames[ctx.getRuleIndex()];
	}
	
	private String indent() {
		return Strings.indent(indent);
	}
	
	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		
		if (isDebugEnabled()) {
			System.out.println("## enter " + indent() + ruleName(ctx) + " " + ctx.getText());
		}
		
		++ indent;
		
		logger.onEnterAntlrRule(ruleName(ctx), ctx.getText());
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		
		if (isDebugEnabled()) {
			System.out.println("## exit " + indent() + ruleName(ctx) + " " + ctx.getText());
		}
		
		-- indent;

		logger.onExitAntlrRule(ruleName(ctx), ctx.getText());
	}
}
