package com.neaterbits.compiler.common.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberType;

public final class StaticInitializer extends ComplexMemberDefinition {

	private final ASTSingle<Block> block;
	
	public StaticInitializer(Context context, Block block) {
		super(context);
		
		Objects.requireNonNull(block);
		
		this.block = makeSingle(block);
	}
	
	public Block getBlock() {
		return block.get();
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.STATIC_INITIALIZER;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onStaticInitializer(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(block, recurseMode, iterator);
	}
}
