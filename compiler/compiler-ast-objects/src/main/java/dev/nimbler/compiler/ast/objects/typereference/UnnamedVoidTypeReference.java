package dev.nimbler.compiler.ast.objects.typereference;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.language.common.types.TypeName;

public final class UnnamedVoidTypeReference extends TypeReference {

	public UnnamedVoidTypeReference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDebugName() {
		return "unnamed_void";
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.UNNAMED_VOID_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onUnnamedVoid(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
