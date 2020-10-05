package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class ElseIfConditionBlock extends ConditionBlock {
    
    private final ASTSingle<Keyword> elseIfKeyword;

    public ElseIfConditionBlock(Context context, Keyword elseIfKeyword, Expression condition,
            Block block) {
        super(context, condition, block);
        
        Objects.requireNonNull(elseIfKeyword);
        
        if (elseIfKeyword.getContext().getStartOffset() != context.getStartOffset()) {
            throw new IllegalArgumentException("offset mismatch " + elseIfKeyword.getContext().getStartOffset() + "/" + context.getStartOffset());
        }

        this.elseIfKeyword = makeSingle(elseIfKeyword);
    }

    public Keyword getElseIfKeyword() {
        return elseIfKeyword.get();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.ELSE_IF_CONDITION_BLOCK;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(elseIfKeyword, recurseMode, iterator);

        super.doRecurse(recurseMode, iterator);
    }
}
