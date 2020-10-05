package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.parse.context.Context;

public class StackIfConditionBlock<
        EXPRESSION,
        NESTED_EXPRESSION extends EXPRESSION,
        PRIMARY extends EXPRESSION,
        VARIABLE_REFERENCE extends PRIMARY,
        STATEMENT> 


    extends StackConditionBlock<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> {

    private final String ifKeyword;
    private final Context ifKeywordContext;

    public StackIfConditionBlock(ParseLogger parseLogger, Context updatedContext, String ifKeyword, Context ifKeywordContext) {
        super(parseLogger, updatedContext);

        this.ifKeyword = ifKeyword;
        this.ifKeywordContext = ifKeywordContext;
    }

    public String getIfKeyword() {
        return ifKeyword;
    }

    public Context getIfKeywordContext() {
        return ifKeywordContext;
    }
}
