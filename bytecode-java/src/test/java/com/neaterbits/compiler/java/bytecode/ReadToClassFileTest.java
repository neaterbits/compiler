package com.neaterbits.compiler.java.bytecode;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.java.bytecode.reader.BaseClassFileReaderTest;
import com.neaterbits.compiler.java.bytecode.reader.DebugReaderListener;

public class ReadToClassFileTest extends BaseClassFileReaderTest {

	@Test
	public void testReadClassFile() throws IOException, ClassFileException {
	
		readClass(new DebugReaderListener(new ClassFile()));
	}
}
