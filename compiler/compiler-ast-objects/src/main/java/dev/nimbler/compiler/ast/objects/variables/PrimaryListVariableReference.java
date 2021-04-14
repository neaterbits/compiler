package dev.nimbler.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class PrimaryListVariableReference extends VariableReference {

	private final ASTSingle<PrimaryList> list;

	public PrimaryListVariableReference(Context context, PrimaryList list) {
		super(context);
		
		Objects.requireNonNull(list);
		
		this.list = makeSingle(list);
	}

	public PrimaryList getList() {
		return list.get();
	}
	
	@Override
	public TypeReference getType() {
		return list.get().getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PRIMARY_LIST_VARIABLE_REFERENCE;
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onPrimaryList(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(list, recurseMode, iterator);
	}
}
