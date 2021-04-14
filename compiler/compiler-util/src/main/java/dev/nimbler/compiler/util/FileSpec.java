package dev.nimbler.compiler.util;

import java.io.File;

public interface FileSpec {
    
    File getFile();

	String getParseContextName();
	
	String getDistinctName();
	
	default String getDebugName() {
		return getParseContextName();
	}
}
