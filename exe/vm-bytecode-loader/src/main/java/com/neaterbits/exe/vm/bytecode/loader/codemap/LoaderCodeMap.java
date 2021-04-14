package com.neaterbits.exe.vm.bytecode.loader.codemap;

import com.neaterbits.language.codemap.CodeMap;

public interface LoaderCodeMap extends CodeMap {

	void addMethodCall(int calledFrom, int calledTo);
	
	int recurseCallGraph(int fromMethodNo, MethodRef methodRef);
}
