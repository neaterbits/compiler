package com.neaterbits.compiler.ast.objects.typereference;

import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class TypeDefTypeReference extends ResolvedNamedTypeReference {

	private final TypeReference aliasedType;

	public TypeDefTypeReference(Context context, int typeNo, TypeName type, TypeReference aliasedType) {
		super(context, typeNo, type);

		Objects.requireNonNull(type);
		Objects.requireNonNull(aliasedType);

		this.aliasedType = aliasedType;
	}

	public TypeReference getAliasedType() {
		return aliasedType;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.TYPEDEF_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onTypeDefTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
