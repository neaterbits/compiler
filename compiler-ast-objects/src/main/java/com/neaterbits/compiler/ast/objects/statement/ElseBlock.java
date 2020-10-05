package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

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
