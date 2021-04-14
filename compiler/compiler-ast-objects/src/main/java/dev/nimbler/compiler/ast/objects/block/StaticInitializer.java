package dev.nimbler.compiler.ast.objects.block;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberType;
import dev.nimbler.compiler.types.ParseTreeElement;

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
