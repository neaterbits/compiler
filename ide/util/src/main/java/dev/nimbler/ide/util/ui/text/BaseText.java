package dev.nimbler.ide.util.ui.text;

abstract class BaseText implements Text {

	final void checkSubstringParams(long beginIndex) {
		checkSubstringParams(this, beginIndex);
	}

	static void checkSubstringParams(Text text, long beginIndex) {

		if (beginIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		if (beginIndex > text.length()) {
			throw new IllegalArgumentException();
		}
	}

	final void checkSubstringParams(long beginIndex, long endIndex) {
		checkSubstringParams(this, beginIndex, endIndex);
	}

	static void checkSubstringParams(Text text, long beginIndex, long endIndex) {
		checkSubstringParams(text, beginIndex);
		
		if (endIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		if (endIndex < beginIndex) {
			throw new IllegalArgumentException();
		}
		
		if (endIndex > text.length()) {
			throw new IllegalArgumentException();
		}
	}


	@Override
	public String toString() {
		return "\"" + asString() + "\"";
	}
}
