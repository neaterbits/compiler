package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;

public abstract class CharacterType extends ScalarType {

	private final int charWidth;
	
	public CharacterType(TypeName name, boolean nullable, int charWidth) {
		super(name, nullable);
		
		this.charWidth = charWidth;
	}

	public int getCharWidth() {
		return charWidth;
	}
}
