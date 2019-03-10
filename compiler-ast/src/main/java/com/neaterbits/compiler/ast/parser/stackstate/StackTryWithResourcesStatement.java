package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Resource;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackTryWithResourcesStatement extends BaseStackTryCatchFinally {

	private List<Resource> resources;
	
	public StackTryWithResourcesStatement(ParseLogger parseLogger) {
		super(parseLogger);
		
	}
	
	public void setResources(List<Resource> resources) {
		
		Objects.requireNonNull(resources);

		if (this.resources != null) {
			throw new IllegalStateException("Resources already set");
		}

		this.resources = resources;
	}

	public List<Resource> getResources() {
		return resources;
	}
}
