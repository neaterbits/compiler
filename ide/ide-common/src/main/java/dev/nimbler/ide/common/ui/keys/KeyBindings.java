package dev.nimbler.ide.common.ui.keys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.common.ui.actions.Action;

public final class KeyBindings {

	private final List<KeyBinding> bindings;
	
	public KeyBindings(Collection<KeyBinding> bindings) {
	
		this.bindings = new ArrayList<>(bindings);
	}

	public KeyCombination findKeyCombination(Action<?, ?> action) {
		
		Objects.requireNonNull(action);

		return bindings.stream()
				.filter(binding -> binding.getAction().equals(action))
				.map(binding -> binding.getKeyCombination())
				.findFirst()
				.orElse(null);
	}
	
	public Action<?, ?> findAction(Key key, KeyMask qualifierMask) {
		
		final KeyCombination keyCombination = new KeyCombination(key, qualifierMask);
	
		for (KeyBinding binding : bindings) {
			
			if (binding.getKeyCombination().matches(keyCombination)) {
				return binding.getAction();
			}
		}
		
		return null;
	}
}
