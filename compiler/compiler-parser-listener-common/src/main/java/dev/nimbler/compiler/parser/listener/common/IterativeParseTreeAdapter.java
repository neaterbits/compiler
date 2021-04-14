package dev.nimbler.compiler.parser.listener.common;

import com.neaterbits.util.parse.context.Context;

public class IterativeParseTreeAdapter<COMPILATION_UNIT>
    extends InfixParseTreeAdapter<COMPILATION_UNIT> 
    implements IterativeParseTreeListener<COMPILATION_UNIT> {

    @Override
    public void onIfStatementStart(int ifStartContext, long ifKeyword, int ifKeywordContext) {
        
    }

    @Override
    public void onIfStatementInitialBlockStart(int ifStatementInitialBlockStartContext) {
        
    }

    @Override
    public void onIfStatementInitialBlockEnd(int ifStartContext, Context endContext) {
        
    }

    @Override
    public void onElseIfStatementStart(int elseIfStartContext, long elseKeyword, int elseKeywordContext, long ifKeyword, int ifKeywordContext) {
        
    }

    @Override
    public void onElseIfStatementEnd(int elseIfStartContext, Context endContext) {
        
    }

    @Override
    public void onElseStatementStart(int elseStartContext, long elseKeyword, int elseKeywordContext) {
        
    }

    @Override
    public void onElseStatementEnd(int elseStartContext, Context elseEndContext) {
        
    }

    @Override
    public void onEndIfStatement(int ifStartContext, Context endIfContext) {
        
    }

    @Override
    public void onSwitchStatementStart(int startContext, long keyword, int keywordContext) {
        
    }

    @Override
    public void onJavaSwitchBlockStart(int startContext) {
        
    }

    @Override
    public void onJavaSwitchBlockStatementGroupStart(int startContext) {
        
    }

    @Override
    public void onSwitchLabelsStart(int startContext) {
        
    }

    @Override
    public void onSwitchLabelsEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onJavaSwitchBlockStatementGroupEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onConstantSwitchLabelStart(int startContext, long keyword, int keywordContext) {
        
    }

    @Override
    public void onConstantSwitchLabelEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onEnumSwitchLabel(int leafContext, long keyword, int keywordContext, long constantName, int constantNameContext) {
        
    }

    @Override
    public void onDefaultSwitchLabel(int leafContext, long keyword) {
        
    }

    @Override
    public void onJavaSwitchBlockEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onSwitchStatementEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onBreakStatement(int startContext, long keyword, int keywordContext, long label, int labelContext, Context endContext) {
        
    }
}
