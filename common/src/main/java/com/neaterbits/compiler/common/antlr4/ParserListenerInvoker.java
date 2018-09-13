package com.neaterbits.compiler.common.antlr4;

public abstract class ParserListenerInvoker {
	private final String [] ruleNames;
	private final ParserContextImpl lCtx;
	
	protected abstract void invokeEnterForRule(int ruleIndex, EnterParserContext ctx);
	
	protected abstract void invokeExitForRule(int ruleIndex, ExitParserContext ctx);
	
	protected ParserListenerInvoker(String [] ruleNames) {
		this.ruleNames = ruleNames;
		this.lCtx = new ParserContextImpl();
	}
	
	/*
	private void initCtxForInvocation(int ruleIndex, String text,
			int startLine, int startColumn,
			int endLine, int endColumn) {
		lCtx.setText(text);
		lCtx.setRuleName(ruleNames[ruleIndex]);
	}
	*/

	public final void invokeEnter(int ruleIndex, EnterParserContext ctx) {
			
		//initCtxForInvocation(ruleIndex, text, startLine, startColumn, endLine, endColumn);
		
		invokeEnterForRule(ruleIndex, ctx);
	}

	public final void invokeExit(int ruleIndex, ExitParserContext ctx) {
		
		//initCtxForInvocation(ruleIndex, text, startLine, startColumn, endLine, endColumn);
		
		invokeExitForRule(ruleIndex, lCtx);
	}
}
