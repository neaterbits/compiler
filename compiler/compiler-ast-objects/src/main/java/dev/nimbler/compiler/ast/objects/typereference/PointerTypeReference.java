package dev.nimbler.compiler.ast.objects.typereference;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.type.NamedType;
import dev.nimbler.compiler.ast.objects.type.PointerType;
import dev.nimbler.language.common.types.TypeName;

public final class PointerTypeReference extends ResolvedTypeReference {

	private final PointerType pointerType;

	public PointerTypeReference(Context context, int typeNo, PointerType pointerType) {
		super(context, typeNo);

		Objects.requireNonNull(pointerType);

		this.pointerType = pointerType;
	}

	private PointerTypeReference(PointerTypeReference other) {
	    super(other);
	    
	    this.pointerType = other.pointerType;
	}
	
	@Override
    public ResolvedTypeReference makeCopy() {
        return new PointerTypeReference(this);
    }

    @Override
	public String getDebugName() {
		return getTypeName().getName() + "_ptr";
	}

	public PointerType getType() {
		return pointerType;
	}

	@Override
	public TypeName getTypeName() {
		return ((NamedType)pointerType.getDelegate()).getTypeName();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onPointerTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
