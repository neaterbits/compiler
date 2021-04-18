package dev.nimbler.compiler.ast.objects.typereference;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.language.common.types.TypeName;

public abstract class ResolvedNamedTypeReference extends ResolvedTypeReference {

	private final TypeName typeName;

	public ResolvedNamedTypeReference(Context context, int typeNo, TypeName typeName) {
		super(context, typeNo);

		Objects.requireNonNull(typeName);

		this.typeName = typeName;
	}

	protected ResolvedNamedTypeReference(ResolvedNamedTypeReference other) {
	    super(other);
	    
	    this.typeName = other.typeName;
	}

	@Override
	public final TypeName getTypeName() {
		return typeName;
	}

	@Override
	public String getDebugName() {
		return typeName.getName();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}


	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResolvedNamedTypeReference other = (ResolvedNamedTypeReference) obj;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}
}
