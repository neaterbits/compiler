package dev.nimbler.compiler.ast.objects.typereference;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.language.common.types.TypeName;

public final class FunctionPointerTypeReference extends ResolvedTypeReference {

	private final FunctionPointerType type;

	public FunctionPointerTypeReference(Context context, int typeNo, FunctionPointerType type) {
		super(context, typeNo);

		Objects.requireNonNull(type);

		this.type = type;
	}
	
	private FunctionPointerTypeReference(FunctionPointerTypeReference other) {
	    super(other);
	    
	    this.type = other.type;
	}
	
	@Override
    public ResolvedTypeReference makeCopy() {
        return new FunctionPointerTypeReference(this);
    }

    public FunctionPointerType getType() {
		return type;
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDebugName() {
		return type.toString();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onFunctionPointerTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
