package dev.nimbler.compiler.parser.listener.stackbased.state;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackElseIfConditionBlock<
            EXPRESSION,
            NESTED_EXPRESSION extends EXPRESSION,
            PRIMARY extends EXPRESSION,
            VARIABLE_REFERENCE extends PRIMARY,
            STATEMENT> 
        
        extends StackConditionBlock<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> {

    private final String elseKeyword;
    private final Context elseKeywordContext;
    private final String ifKeyword;
    private final Context ifKeywordContext;
    
    public StackElseIfConditionBlock(
            ParseLogger parseLogger,
            Context updatedContext,
            String elseKeyword,
            Context elseKeywordContext,
            String ifKeyword,
            Context ifKeywordContext) {
        
        super(parseLogger, updatedContext);
        
        this.elseKeyword = elseKeyword;
        this.elseKeywordContext = elseKeywordContext;
        
        this.ifKeyword = ifKeyword;
        this.ifKeywordContext = ifKeywordContext;
    }

    public String getElseKeyword() {
        return elseKeyword;
    }

    public Context getElseKeywordContext() {
        return elseKeywordContext;
    }

    public String getIfKeyword() {
        return ifKeyword;
    }

    public Context getIfKeywordContext() {
        return ifKeywordContext;
    }
}
