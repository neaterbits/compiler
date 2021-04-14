package com.neaterbits.compiler.bytecode.common.loader.codemap;

import com.neaterbits.compiler.codemap.CodeMap;

public interface LoaderCodeMap extends CodeMap {

	void addMethodCall(int calledFrom, int calledTo);
	
	int recurseCallGraph(int fromMethodNo, MethodRef methodRef);
}
