package com.neaterbits.language.bytecode.java.reader;

import java.io.IOException;

@FunctionalInterface
public interface OnAttribute {

	void onAttribute(int attributeIndex, int nameIndex, int attributesLength) throws IOException;

}
