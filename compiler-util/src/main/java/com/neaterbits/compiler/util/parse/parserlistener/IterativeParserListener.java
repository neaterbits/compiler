package com.neaterbits.compiler.util.parse.parserlistener;

import com.neaterbits.compiler.util.Context;

public interface IterativeParserListener<COMPILATION_UNIT> extends InfixParserListener<COMPILATION_UNIT> {

	void onIfStatementStart(Context context, String ifKeyword, Context ifKeywordContext);
	
	void onIfStatementInitialBlockEnd(Context context);
	
	void onElseIfStatementStart(Context context, String elseKeyword, Context elseKeywordContext, String ifKeyword, Context ifKeywordContext);
	
	void onElseIfStatementEnd(Context context);

	void onElseStatementStart(Context context, String keyword, Context keywordContext);
	
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
