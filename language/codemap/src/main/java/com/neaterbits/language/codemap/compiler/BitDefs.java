package com.neaterbits.language.codemap.compiler;

class BitDefs {

	static final int TOKEN_BITS = 30;
	static final int MAX_TOKEN_VALUE = (1 << TOKEN_BITS) - 1;

	static final int PARSE_TREE_REF_BITS = 18;
	static final int MAX_PARSE_TREE_REF = (1 << PARSE_TREE_REF_BITS) - 1;

	static final int SOURCE_FILE_BITS = 16;
	static final int MAX_SOURCE_FILE = (1 << SOURCE_FILE_BITS) - 1; 

	static final int WARNING_WORKAROUND = Integer.parseInt("0");
	
	static {
		// For max utilization of hash mapping
		if (TOKEN_BITS + PARSE_TREE_REF_BITS + SOURCE_FILE_BITS != 64 + WARNING_WORKAROUND) {
			throw new IllegalStateException();
		}
	}
}
