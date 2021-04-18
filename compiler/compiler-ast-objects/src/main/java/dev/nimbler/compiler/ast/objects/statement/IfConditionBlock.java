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
