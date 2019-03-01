package com.neaterbits.compiler.bytecode.common;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.neaterbits.compiler.common.TypeName;

public interface BytecodeFormat {

	Set<TypeName> getTypesFromLibraryFile(File libraryPath) throws IOException;

	ClassBytecode loadClassBytecode(ClassLibs classLibs, TypeName className) throws IOException, ClassFileException;
	
}
