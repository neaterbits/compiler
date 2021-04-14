package com.neaterbits.ide.common.ui.keys;

import java.util.Objects;

public final class KeyCombination {

	private final Key key;
	private final KeyMask qualifiers;

	KeyCombination(Key key) {

		Objects.requireNonNull(key);
		
		this.key = key;
		this.qualifiers = null;
	}

	public KeyCombination(Key key, KeyMask qualifiers) {
		
		Objects.requireNonNull(key);
		Objects.requireNonNull(qualifiers);
		
		this.key = key;
		this.qualifiers = qualifiers;
	}

	public Key getKey() {
		return key;
	}

	public KeyMask getQualifiers() {
		return qualifiers;
	}
	
	boolean matches(KeyCombination other) {
		return key.matches(other.key) && Objects.equals(qualifiers, other.qualifiers);
	}

	@Override
	public String toString() {
		return "KeyCombination [key=" + key + ", qualifiers=" + qualifiers + "]";
	}
}
