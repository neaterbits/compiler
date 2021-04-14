package dev.nimbler.compiler.ast.objects.typereference;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.language.common.types.TypeName;

public final class TypeDefTypeReference extends ResolvedNamedTypeReference {

	private final TypeReference aliasedType;

	public TypeDefTypeReference(Context context, int typeNo, TypeName type, TypeReference aliasedType) {
		super(context, typeNo, type);

		Objects.requireNonNull(type);
		Objects.requireNonNull(aliasedType);

		this.aliasedType = aliasedType;
	}
	
	private TypeDefTypeReference(TypeDefTypeReference other) {
	    super(other);
	    
	    this.aliasedType = other.aliasedType;
	}

	@Override
    public ResolvedTypeReference makeCopy() {
        return new TypeDefTypeReference(this);
    }

    public TypeReference getAliasedType() {
		return aliasedType;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onTypeDefTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
