package com.neaterbits.compiler.ast.objects.block;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ComplexMemberType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.STATIC_INITIALIZER;
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
