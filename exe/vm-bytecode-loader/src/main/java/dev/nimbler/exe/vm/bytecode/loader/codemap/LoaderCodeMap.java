package dev.nimbler.exe.vm.bytecode.loader.codemap;

import dev.nimbler.language.codemap.CodeMap;

public interface LoaderCodeMap extends CodeMap {

	void addMethodCall(int calledFrom, int calledTo);
	
	int recurseCallGraph(int fromMethodNo, MethodRef methodRef);
}
