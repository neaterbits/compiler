package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.compiler.codemap.CodeMap;

public interface CompilerCodeMap extends CodeMap {

	int addFile(String file, int [] types);

	void removeFile(String file);

	int addToken(int sourceFile, int tokenOffset, int tokenLength);
}
