package com.neaterbits.compiler.parser.listener.common;

import com.neaterbits.compiler.util.Context;

public interface IterativeParserListener<COMPILATION_UNIT> extends InfixParserListener<COMPILATION_UNIT> {

	void onIfStatementStart(Context context, long ifKeyword, Context ifKeywordContext);
	
	void onIfStatementInitialBlockEnd(Context context);
	
	void onElseIfStatementStart(Context context, long elseIfKeyword, Context elseIfKeywordContext);
	
	void onElseIfStatementEnd(Context context);

	void onElseStatementStart(Context context, long elseKeyword, Context elseKeywordContext);
	
	void onElseStatementEnd(Context context);

	void onEndIfStatement(Context context);
	
	void onSwitchStatementStart(Context context, String keyword, Context keywordContext);

	void onJavaSwitchBlockStart(Context context);
	
	void onJavaSwitchBlockStatementGroupStart(Context context);

	void onSwitchLabelsStart(Context context);
	
	void onSwitchLabelsEnd(Context context);
	
	void onJavaSwitchBlockStatementGroupEnd(Context context);
	
	void onConstantSwitchLabelStart(Context context, String keyword, Context keywordContext);
	
	void onConstantSwitchLabelEnd(Context context);
	
	void onEnumSwitchLabel(
			Context context,
			String keyword, Context keywordContext,
			String constantName, Context constantNameContext);
	
	void onDefaultSwitchLabel(Context context, String keyword, Context keywordContext);
	
	void onJavaSwitchBlockEnd(Context context);
	
	void onSwitchStatementEnd(Context context);
	
	void onBreakStatement(Context context, String keyword, Context keywordContext, String label);
}
