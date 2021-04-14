package dev.nimbler.compiler.ast.objects.type.primitive;

import dev.nimbler.compiler.util.name.BaseTypeName;

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
