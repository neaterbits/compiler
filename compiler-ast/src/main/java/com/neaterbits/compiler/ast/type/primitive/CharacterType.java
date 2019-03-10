package com.neaterbits.compiler.ast.type.primitive;

import com.neaterbits.compiler.ast.type.BaseTypeName;

public abstract class CharacterType extends ScalarType {

	private final int charWidth;
	
	public CharacterType(BaseTypeName name, boolean nullable, int charWidth) {
		super(name, nullable);
		
		this.charWidth = charWidth;
	}

	public int getCharWidth() {
		return charWidth;
	}
}
