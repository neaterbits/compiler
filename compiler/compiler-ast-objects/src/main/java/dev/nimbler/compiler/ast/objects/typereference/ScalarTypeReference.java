package dev.nimbler.compiler.ast.objects.typereference;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.language.common.types.TypeName;

public final class ScalarTypeReference extends BuiltinTypeReference {

	public ScalarTypeReference(Context context, int typeNo, TypeName type) {
		super(context, typeNo, type);
	}

	private ScalarTypeReference(ScalarTypeReference other) {
	    super(other);
	}

	@Override
    public ResolvedTypeReference makeCopy() {
        return new ScalarTypeReference(this);
    }

    @Override
	public boolean isScalar() {
		return true;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onScalarTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
