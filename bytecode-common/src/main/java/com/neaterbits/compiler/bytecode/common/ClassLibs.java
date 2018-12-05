package com.neaterbits.compiler.bytecode.common;

import java.io.IOException;

import com.neaterbits.compiler.common.TypeName;

public interface ClassLibs {

	ClassBytecode loadClassBytecode(TypeName className) throws IOException, ClassFileException;
	
}
