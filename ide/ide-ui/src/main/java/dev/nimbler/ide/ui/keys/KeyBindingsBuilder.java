package dev.nimbler.ide.ui.keys;

import java.util.ArrayList;
import java.util.List;

import dev.nimbler.ide.common.ui.keys.Key;
import dev.nimbler.ide.common.ui.keys.KeyBinding;
import dev.nimbler.ide.common.ui.keys.KeyBindings;
import dev.nimbler.ide.common.ui.keys.KeyMask;
import dev.nimbler.ide.common.ui.keys.QualifierKey;
import dev.nimbler.ide.ui.actions.BuiltinAction;

final class KeyBindingsBuilder {

	private final List<KeyBinding> keyBindings;
	
	KeyBindingsBuilder() {
		this.keyBindings = new ArrayList<>();
	}
	
	KeyBindingsBuilder addBuiltinAction(BuiltinAction builtinAction, Key key, QualifierKey ... qualifierKeys) {
		keyBindings.add(new KeyBinding(builtinAction.getAction(), key, new KeyMask(qualifierKeys)));
	
		return this;
	}

	KeyBindingsBuilder addBuiltinAction(BuiltinAction builtinAction, char character, QualifierKey ... qualifierKeys) {
		keyBindings.add(new KeyBinding(builtinAction.getAction(), new Key(character), new KeyMask(qualifierKeys)));
	
		return this;
	}
	
	KeyBindings build() {
		return new KeyBindings(keyBindings);
	}
}
