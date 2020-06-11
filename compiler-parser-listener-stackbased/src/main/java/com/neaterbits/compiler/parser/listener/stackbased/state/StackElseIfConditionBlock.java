package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackElseIfConditionBlock<
            EXPRESSION,
            PRIMARY extends EXPRESSION,
            VARIABLE_REFERENCE extends PRIMARY,
            STATEMENT> 
        
        extends StackConditionBlock<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> {

    private final String elseIfKeyword;
    private final Context elseIfKeywordContext;
    
    public StackElseIfConditionBlock(
            ParseLogger parseLogger,
            Context updatedContext,
            String elseIfKeyword,
            Context elseIfKeywordContext) {
        
        super(parseLogger, updatedContext);
        
        this.elseIfKeyword = elseIfKeyword;
        this.elseIfKeywordContext = elseIfKeywordContext;
    }

    public String getElseIfKeyword() {
        return elseIfKeyword;
    }

    public Context getElseIfKeywordContext() {
        return elseIfKeywordContext;
    }
}
