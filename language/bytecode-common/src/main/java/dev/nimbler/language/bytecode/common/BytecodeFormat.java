package dev.nimbler.language.bytecode.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.TypeSource;
import dev.nimbler.language.common.typesources.libs.ClassLibs;

public interface BytecodeFormat {

	Set<TypeName> getTypesFromLibraryFile(File libraryPath) throws IOException;

	ClassByteCodeWithTypeSource loadClassBytecode(ClassLibs classLibs, TypeName typeName) throws IOException, ClassFileException;
	
	ClassByteCodeWithTypeSource loadClassBytecode(InputStream inputStream, TypeSource typeSource) throws IOException, ClassFileException;

	ClassByteCodeWithTypeSource loadClassBytecode(File library, TypeName typeName, TypeSource typeSource) throws IOException, ClassFileException;

}
