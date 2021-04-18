package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ElseBlock extends BaseASTElement {

    private final ASTSingle<Block> block;

    public ElseBlock(Context context, Block block) {
        super(context);

        Objects.requireNonNull(block);
        
        this.block = makeSingle(block);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.ELSE_BLOCK;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(block, recurseMode, iterator);
        
    }
}
