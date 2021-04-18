package dev.nimbler.compiler.ast.objects.typereference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.generics.TypeArgument;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.ReferenceType;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

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
		throw new UnsupportedOperationException("No typename for " + scopedName);
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
