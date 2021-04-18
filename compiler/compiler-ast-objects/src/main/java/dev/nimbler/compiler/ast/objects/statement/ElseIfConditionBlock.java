package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

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
