package com.neaterbits.language.bytecode.common;

import com.neaterbits.language.common.typesources.TypeSource;

// Separate interface for source information
// this is metadata, not really part of the class format

public interface ClassByteCodeWithTypeSource extends ClassBytecode {

	TypeSource getTypeSource();
	
}
