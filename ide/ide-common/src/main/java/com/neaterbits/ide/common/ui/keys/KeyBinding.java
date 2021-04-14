package com.neaterbits.ide.common.ui.keys;

import java.util.Objects;

import com.neaterbits.ide.common.ui.actions.Action;

public final class KeyBinding {

	private final Action<?, ?> action;
	private final KeyCombination keyCombination;
	
	public KeyBinding(Action<?, ?> action, Key key) {
		
		Objects.requireNonNull(action);
		Objects.requireNonNull(key);
		
		this.action = action;
		this.keyCombination = new KeyCombination(key);
	}

	public KeyBinding(Action<?, ?> action, Key key, KeyMask qualifiers) {
		
		Objects.requireNonNull(action);
		
		this.action = action;
		this.keyCombination = new KeyCombination(key, qualifiers);
	}

	public Action<?, ?> getAction() {
		return action;
	}

	public Key getKey() {
		return keyCombination.getKey();
	}

	public KeyMask getQualifiers() {
		return keyCombination.getQualifiers();
	}

	KeyCombination getKeyCombination() {
		return keyCombination;
	}
}
