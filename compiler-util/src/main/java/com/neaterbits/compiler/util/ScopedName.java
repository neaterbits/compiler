package com.neaterbits.compiler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ScopedName {

	private final List<String> scope;
	private final String name;

	public static ScopedName makeScopedName(String [] namespaceParts, String name) {
		return makeScopedName(namespaceParts, namespaceParts.length, name);
	}

	public static ScopedName makeScopedName(String [] parts, int num, String name) {
		
		final List<String> scope = new ArrayList<>(num + 1);
		
		for (int i = 0; i < num; ++ i) {
			scope.add(parts[i]);
		}

		return new ScopedName(scope, name);
	}
	
	public ScopedName(List<String> scope, String name) {
		
		Objects.requireNonNull(name);
		
		this.scope = scope != null ? Collections.unmodifiableList(scope) : null;
		this.name = name;
	}

	public List<String> getScope() {
		return scope;
	}
	
	public boolean hasScope() {
		return scope != null;
	}
	
	public String [] getParts() {
		
		final String [] parts;
		
		if (scope != null) {
			parts = scope.toArray(new String[scope.size() + 1]);

			parts[parts.length - 1] = name;
		}
		else {
			parts = new String [] { name };
		}
		
		return parts;
	}

	public String getName() {
		return name;
	}
	
	public boolean scopeStartsWith(String [] parts) {
		return scope != null ? Strings.startsWith(scope, parts) : false;
	}
	
	public ScopedName removeFromScope(String [] parts) {
		if (!scopeStartsWith(parts)) {
			throw new IllegalArgumentException("Does not start with scope " + Arrays.toString(parts));
		}

		return new ScopedName(scope.subList(parts.length, scope.size()), name);
	}

	@Override
	public String toString() {
		return (scope != null ? Strings.join(scope, '.') : "") + '/' + name; 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
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
		ScopedName other = (ScopedName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}
}