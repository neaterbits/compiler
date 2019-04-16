package com.neaterbits.compiler.util;

public interface FileSpec {

	String getParseContextName();
	
	default String getDebugName() {
		return getParseContextName();
	}
}
