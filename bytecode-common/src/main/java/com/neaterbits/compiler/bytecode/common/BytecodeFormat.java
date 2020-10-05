package com.neaterbits.compiler.bytecode.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.neaterbits.build.types.ClassLibs;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.TypeSource;

public interface BytecodeFormat {

	Set<TypeName> getTypesFromLibraryFile(File libraryPath) throws IOException;

	ClassByteCodeWithTypeSource loadClassBytecode(ClassLibs classLibs, TypeName typeName) throws IOException, ClassFileException;
	
	ClassByteCodeWithTypeSource loadClassBytecode(InputStream inputStream, TypeSource typeSource) throws IOException, ClassFileException;

	ClassByteCodeWithTypeSource loadClassBytecode(File library, TypeName typeName, TypeSource typeSource) throws IOException, ClassFileException;

}
