package com.neaterbits.language.bytecode.java.reader;

import java.io.DataInput;
import java.io.IOException;

public abstract class ClassFileAndAttributesReaderListener implements ClassFileReaderListener {

	private String [] utf8Constants;
	
	@Override
	public void onConstantPoolCount(int count) {
		this.utf8Constants = new String[count];
	}

	@Override
	public void onConstantPoolUTF8(int index, String value) {
		utf8Constants[index] = value;
	}

	protected final void parseAttribute(int memberIndex, int attributeNameIndex, int attributeLength,
			DataInput dataInput, ClassFileAttributesListener attributesListener) throws IOException {

		final String constant = utf8Constants[attributeNameIndex];

		ClassFileReader.parseAttribute(constant, memberIndex, attributeLength, dataInput, attributesListener);
	}
}
