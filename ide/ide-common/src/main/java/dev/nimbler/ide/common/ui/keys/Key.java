package dev.nimbler.ide.common.ui.keys;

public class Key {
	public static final int F1 	= 0x10000001;
	public static final int F2 	= 0x10000002;
	public static final int F3 	= 0x10000003;
	public static final int F4 	= 0x10000004;
	public static final int F5 	= 0x10000005;
	public static final int F6 	= 0x10000006;
	public static final int F7 	= 0x10000007;
	public static final int F8 	= 0x10000008;
	public static final int F9 	= 0x10000009;
	public static final int F10 = 0x10000010;
	public static final int F11 = 0x10000011;
	public static final int F12 = 0x10000012;
	
	private final char character;
	private final int keyCode;

	public Key(char character) {
		this(character, -1);
	}

	public Key(char character, int keyCode) {
		this.character = character;
		this.keyCode = keyCode;
	}

	public char getCharacter() {
		return character;
	}

	public int getKeyCode() {
		return keyCode;
	}
	
	public boolean matches(Key other) {
		
		final boolean matches;
		
		if (keyCode != -1 && other.keyCode != -1) {
			matches = keyCode == other.keyCode;
		}
		else if (character != -1 && other.character != -1) {
			matches = character == other.character;
		}
		else {
			matches = false;
		}
		
		return matches;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + character;
		result = prime * result + keyCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (character != other.character)
			return false;
		if (keyCode != other.keyCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Key [character=" + character + ", keyCode=" + keyCode + "]";
	}
}
