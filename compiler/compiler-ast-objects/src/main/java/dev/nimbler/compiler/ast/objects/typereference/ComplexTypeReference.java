package dev.nimbler.compiler.ast.objects.typereference;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.language.common.types.TypeName;

public final class ComplexTypeReference extends ResolvedNamedTypeReference {

	public ComplexTypeReference(Context context, int typeNo, TypeName typeName) {
		super(context, typeNo, typeName);
	}
	
	private ComplexTypeReference(ComplexTypeReference other) {
	    super(other);
	}

	@Override
    public ResolvedTypeReference makeCopy() {
        return new ComplexTypeReference(this);
    }

    @Override
    public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onComplexTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
