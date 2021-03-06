package dev.nimbler.compiler.ast.objects.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.nimbler.compiler.util.name.BaseTypeName;
import dev.nimbler.compiler.util.name.DefinitionName;
import dev.nimbler.compiler.util.name.NamespaceReference;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

public final class CompleteName {

	private final NamespaceReference namespace; // "package" in Java
	private final List<DefinitionName> outerTypes; // If this is an inner class
	private final BaseTypeName name; // Name of the type itself

	public CompleteName(NamespaceReference namespace, List<DefinitionName> outerTypes, BaseTypeName name) {
		this.namespace = namespace;
		this.outerTypes = outerTypes != null ? Collections.unmodifiableList(outerTypes) : null;
		this.name = name;
	}
	
	public NamespaceReference getNamespace() {
		return namespace;
	}

	public List<DefinitionName> getOuterTypes() {
		return outerTypes;
	}

	public BaseTypeName getName() {
		return name;
	}
	
	public TypeName toTypeName() {
		
		final String [] outerTypesArray;
		
		if (outerTypes == null) {
			outerTypesArray = null;
		}
		else {
			outerTypesArray = new String[outerTypes.size()];
			
			for (int i = 0; i < outerTypes.size(); ++ i) {
				outerTypesArray[i] = outerTypes.get(i).getName();
			}
		}
		
		return new TypeName(
				namespace != null ? namespace.getParts() : null,
				outerTypesArray,
				name.getName());
	}
	
	public ScopedName toScopedName() {
		final List<String> scope = new ArrayList<>(
				
				  (namespace != null ? namespace.getParts().length : 0)
				+ (outerTypes != null ? outerTypes.size() : 0));

		if (namespace != null) {
			final String [] namespaceParts = namespace.getParts();
			
			for (int i = 0; i < namespaceParts.length; ++ i) {
				scope.add(namespaceParts[i]);
			}
		}
		
		if (outerTypes != null) {
			for (DefinitionName name : outerTypes) {
				scope.add(name.getName());
			}
		}
		
		return new ScopedName(scope, name.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((outerTypes == null) ? 0 : outerTypes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompleteName other = (CompleteName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (outerTypes == null) {
			if (other.outerTypes != null)
				return false;
		} else if (!outerTypes.equals(other.outerTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toScopedName().toString();
	}
}
