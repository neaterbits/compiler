package com.neaterbits.compiler.bytecode.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.neaterbits.compiler.util.TypeName;

public interface BytecodeFormat {

	Set<TypeName> getTypesFromLibraryFile(File libraryPath) throws IOException;

	ClassBytecode loadClassBytecode(ClassLibs classLibs, TypeName typeName) throws IOException, ClassFileException;
	
	ClassBytecode loadClassBytecode(InputStream inputStream) throws IOException, ClassFileException;

	ClassBytecode loadClassBytecode(File library, TypeName typeName) throws IOException, ClassFileException;

}
