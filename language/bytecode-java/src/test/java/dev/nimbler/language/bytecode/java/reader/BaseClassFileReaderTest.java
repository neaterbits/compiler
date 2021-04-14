package dev.nimbler.language.bytecode.java.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import dev.nimbler.language.bytecode.common.ClassFileException;

public abstract class BaseClassFileReaderTest {

	protected final void readClass(ClassFileReaderListener readerListener) throws IOException, ClassFileException {

		final File file = new File("target/classes/com/neaterbits/compiler/java/bytecode/reader/ClassFileReader.class");
		
		try (FileInputStream inputStream = new FileInputStream(file)) {

			ClassFileReader.readClassFile(inputStream, readerListener);
		}
	}
}
