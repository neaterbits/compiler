package com.neaterbits.compiler.common.parser.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Resource;
import com.neaterbits.compiler.common.log.ParseLogger;

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
