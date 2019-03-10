package com.neaterbits.compiler.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberType;
import com.neaterbits.compiler.util.Context;

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
