package dev.nimbler.compiler.parser.listener.common;

import org.jutils.parse.context.Context;

public interface IterativeParseTreeListener<COMPILATION_UNIT> extends InfixParseTreeListener<COMPILATION_UNIT> {

	void onIfStatementStart(int ifStartContext, long ifKeyword, int ifKeywordContext);

	void onIfStatementInitialBlockStart(int ifStatementInitialBlockStartContext);

	void onIfStatementInitialBlockEnd(int ifStartContext, Context endContext);
	
	void onElseIfStatementStart(int elseIfStartContext, long elseKeyword, int elseKeywordContext, long ifKeyword, int ifKeywordContext);
	
	void onElseIfStatementEnd(int elseIfStartContext, Context endContext);

	void onElseStatementStart(int elseStartContext, long elseKeyword, int elseKeywordContext);
	
	void onElseStatementEnd(int elseStartContext, Context elseEndContext);

	void onEndIfStatement(int ifStartContext, Context endIfContext);
	
	void onSwitchStatementStart(int startContext, long keyword, int keywordContext);

	void onJavaSwitchBlockStart(int startContext);
	
	void onJavaSwitchBlockStatementGroupStart(int startContext);

	void onSwitchLabelsStart(int startContext);
	
	void onSwitchLabelsEnd(int startContext, Context endContext);
	
	void onJavaSwitchBlockStatementGroupEnd(int startContext, Context endContext);
	
	void onConstantSwitchLabelStart(int startContext, long keyword, int keywordContext);
	
	void onConstantSwitchLabelEnd(int startContext, Context endContext);
	
	void onEnumSwitchLabel(
			int leafContext,
			long keyword, int keywordContext,
			long constantName, int constantNameContext);
	
	void onDefaultSwitchLabel(int leafContext, long keyword);
	
	void onJavaSwitchBlockEnd(int startContext, Context endContext);
	
	void onSwitchStatementEnd(int startContext, Context endContext);
	
	void onBreakStatement(int startContext, long keyword, int keywordContext, long label, int labelContext, Context endContext);
}
