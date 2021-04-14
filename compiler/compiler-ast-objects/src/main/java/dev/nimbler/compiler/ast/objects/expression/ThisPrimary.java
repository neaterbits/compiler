package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.ResolvedPrimary;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ThisPrimary extends ResolvedPrimary {

	private final ASTSingle<TypeReference> type;
	
	public ThisPrimary(Context context, TypeReference type) {
		super(context);
		
		Objects.requireNonNull(type);
	
		this.type = makeSingle(type);
	}

	@Override
	public TypeReference getType() {
		return type.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.THIS_PRIMARY;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onThis(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
