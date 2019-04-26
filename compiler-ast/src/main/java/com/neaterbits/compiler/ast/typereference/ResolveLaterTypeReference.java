package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ReferenceType;

public final class ResolveLaterTypeReference extends TypeReference {

	private final ScopedName scopedName;
	private final ReferenceType referenceType;
	private final int numPointers;

	public ResolveLaterTypeReference(Context context, ScopedName scopedName, ReferenceType referenceType) {
		this(context, scopedName, referenceType, 0);
	}

	public ResolveLaterTypeReference(Context context, ScopedName scopedName, ReferenceType referenceType, int numPointers) {
		super(context);

		Objects.requireNonNull(scopedName);
		Objects.requireNonNull(referenceType);
		
		if (numPointers > 0) {
			if (!referenceType.hasPointers()) {
				throw new IllegalArgumentException();
			}
		}
		else {
			if (referenceType.hasPointers()) {
				throw new IllegalArgumentException();
			}
		}
		
		this.scopedName = scopedName;
		this.referenceType = referenceType;
		this.numPointers = numPointers;
	}

	public ScopedName getScopedName() {
		return scopedName;
	}
	
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public int getNumPointers() {
		return numPointers;
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDebugName() {
		return scopedName.getName();
	}

	@Override
	public String toString() {
		return "ResolveLaterTypeReference [scopedName=" + scopedName + "]";
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onResolveLaterTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
