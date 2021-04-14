package dev.nimbler.language.bytecode.java.reader;

import java.io.IOException;

import org.junit.Test;

import dev.nimbler.language.bytecode.common.ClassFileException;

public class ClassFileReaderTest extends BaseClassFileReaderTest {
	
	@Test
	public void testPrintClassFileLoading() throws IOException, ClassFileException {
	
		readClass(new DebugReaderListener());
	}
}
