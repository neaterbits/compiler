package com.neaterbits.compiler.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.ast.type.BaseType;

public final class ResolveLaterTypeReference extends TypeReference {

	private final List<String> typeName;
	private BaseType resolved;

	public ResolveLaterTypeReference(Context context, String typeName) {
		this(context, Arrays.asList(typeName));
	}

	public ResolveLaterTypeReference(Context context, List<String> typeName) {
		super(context);

		this.typeName = Collections.unmodifiableList(typeName);
	}

	public List<String> getTypeName() {
		return typeName;
	}
	
	public void setResolved(BaseType resolved) {
		this.resolved = resolved;
	}

	@Override
	public BaseType getType() {
		
		if (resolved == null) {
			throw new IllegalStateException("Not yet resolved");
		}

		return resolved;
	}

	@Override
	public String toString() {
		return "ResolveLaterTypeReference [typeName=" + typeName + ", resolved=" + resolved + "]";
	}
}
