package com.neaterbits.compiler.bytecode.common;

import com.neaterbits.compiler.util.model.TypeSource;

// Separate interface for source information
// this is metadata, not really part of the class format

public interface ClassByteCodeWithTypeSource extends ClassBytecode {

	TypeSource getTypeSource();
	
}
