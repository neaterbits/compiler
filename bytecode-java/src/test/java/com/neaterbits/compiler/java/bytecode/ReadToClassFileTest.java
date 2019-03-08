package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.java.bytecode.reader.BaseClassFileReaderTest;
import com.neaterbits.compiler.java.bytecode.reader.DebugReaderListener;

public class ReadToClassFileTest extends BaseClassFileReaderTest {

	@Test
	public void testReadClassFile() throws IOException, ClassFileException {
	
		readClass(new DebugReaderListener(new ClassFile()));
	}
	
	@Test
	public void testLoadClassFromLibrary() throws IOException, ClassFileException {
		
		final String jreDir = System.getProperty("java.home");
		
		final File file = new File(jreDir + "/lib/rt.jar");

		final JavaBytecodeFormat javaBytecodeFormat = new JavaBytecodeFormat();
		
		javaBytecodeFormat.loadClassBytecode(file, new TypeName(
				new String [] { "java", "util"},
				null,
				"HashMap"),
				new DebugReaderListener(new ClassFile()));
		
	}
}
