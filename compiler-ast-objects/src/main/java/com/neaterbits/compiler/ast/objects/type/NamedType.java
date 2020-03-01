package com.neaterbits.compiler.ast.objects.type;

import java.util.Objects;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.name.BaseTypeName;
import com.neaterbits.compiler.util.name.NamespaceReference;

public abstract class NamedType extends BaseType {

	private final CompleteName completeName;
	
	protected NamedType(CompleteName completeName, boolean nullable) {
		super(nullable);

		Objects.requireNonNull(completeName);
		
		this.completeName = completeName;
	}

	public final NamespaceReference getNamespace() {
		return completeName.getNamespace();
	}

	public final BaseTypeName getName() {
		return completeName.getName();
	}
	
	public final CompleteName getCompleteName() {
		return completeName;
	}
	
	public final TypeName getTypeName() {
		return completeName.toTypeName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((completeName == null) ? 0 : completeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedType other = (NamedType) obj;
		if (completeName == null) {
			if (other.completeName != null)
				return false;
		} else if (!completeName.equals(other.completeName))
			return false;
		return true;
	}
}
