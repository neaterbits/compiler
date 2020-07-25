package com.neaterbits.compiler.ast.objects.typereference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.generics.TypeArgument;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.ReferenceType;

public final class UnresolvedTypeReference extends TypeReference {

	private final ScopedName scopedName;
	private final List<TypeArgument> genericTypeParameters;
	private final ReferenceType referenceType;
	private final int numPointers;

	public UnresolvedTypeReference(Context context, ScopedName scopedName, Collection<TypeArgument> genericTypeParameters, ReferenceType referenceType) {
		this(context, scopedName, genericTypeParameters, referenceType, 0);
	}

	public UnresolvedTypeReference(Context context, ScopedName scopedName, Collection<TypeArgument> genericTypeParameters, ReferenceType referenceType, int numPointers) {
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
		this.genericTypeParameters = genericTypeParameters != null
		        ? new ArrayList<>(genericTypeParameters)
		        : null;
		this.referenceType = referenceType;
		this.numPointers = numPointers;
	}

	public ScopedName getScopedName() {
		return scopedName;
	}
	
	public List<TypeArgument> getGenericTypeParameters() {
        return genericTypeParameters;
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
		return "UnresolvedTypeReference [scopedName=" + scopedName + "]";
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.UNRESOLVED_SCOPED_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onResolveLaterTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
