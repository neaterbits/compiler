package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class IfConditionBlock extends ConditionBlock {

    private final ASTSingle<Keyword> ifKeyword;

    public IfConditionBlock(Context context, Keyword ifKeyword, Expression condition, Block block) {
        
        super(context, condition, block);
        
        Objects.requireNonNull(ifKeyword);
        
        this.ifKeyword = makeSingle(ifKeyword);
    }

    public Keyword getIfKeyword() {
        return ifKeyword.get();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.IF_CONDITION_BLOCK;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(ifKeyword, recurseMode, iterator);

        super.doRecurse(recurseMode, iterator);
    }
}
