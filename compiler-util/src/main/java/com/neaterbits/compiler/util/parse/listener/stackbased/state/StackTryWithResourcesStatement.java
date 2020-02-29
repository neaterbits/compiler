package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackTryWithResourcesStatement<STATEMENT, CATCH_BLOCK, RESOURCE>
		extends BaseStackTryCatchFinally<STATEMENT, CATCH_BLOCK> {

	private List<RESOURCE> resources;
	
	public StackTryWithResourcesStatement(ParseLogger parseLogger) {
		super(parseLogger);
		
	}
	
	public void setResources(List<RESOURCE> resources) {
		
		Objects.requireNonNull(resources);

		if (this.resources != null) {
			throw new IllegalStateException("Resources already set");
		}

		this.resources = resources;
	}

	public List<RESOURCE> getResources() {
		return resources;
	}
}
