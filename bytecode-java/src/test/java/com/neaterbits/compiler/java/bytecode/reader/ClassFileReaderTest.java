package com.neaterbits.compiler.java.bytecode.reader;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.bytecode.common.ClassFileException;

public class ClassFileReaderTest extends BaseClassFileReaderTest {
	
	@Test
	public void testPrintClassFileLoading() throws IOException, ClassFileException {
	
		readClass(new DebugReaderListener());
	}
}
