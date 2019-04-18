package com.neaterbits.compiler.util;

public interface FileSpec {

	String getParseContextName();
	
	String getDistinctName();
	
	default String getDebugName() {
		return getParseContextName();
	}
}
