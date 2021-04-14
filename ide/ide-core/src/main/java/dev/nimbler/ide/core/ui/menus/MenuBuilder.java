package dev.nimbler.ide.core.ui.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import dev.nimbler.ide.common.ui.keys.KeyBindings;
import dev.nimbler.ide.common.ui.menus.BuiltinMenu;
import dev.nimbler.ide.common.ui.menus.MenuEntry;
import dev.nimbler.ide.common.ui.menus.SeparatorMenuEntry;
import dev.nimbler.ide.common.ui.menus.SubMenuEntry;
import dev.nimbler.ide.core.ui.actions.BuiltinAction;

final class MenuBuilder {

	private final KeyBindings keyBindings;
	private final List<MenuEntry> entries;
	
	MenuBuilder(KeyBindings keyBindings) {
		
		Objects.requireNonNull(keyBindings);
		
		this.entries = new ArrayList<>();
		this.keyBindings = keyBindings;
	}
	
	MenuBuilder addSubMenu(BuiltinMenu name, Consumer<MenuBuilder> builder) {
	
		final MenuBuilder subBuilder = new MenuBuilder(keyBindings);
		
		builder.accept(subBuilder);

		final List<MenuEntry> subEntries = subBuilder.build();
		
		entries.add(new SubMenuEntry(name, subEntries));
		
		return this;
	}
	
	MenuBuilder addSeparator() {
		
		entries.add(new SeparatorMenuEntry());
	
		return this;
	}

	MenuBuilder addBuiltinAction(BuiltinAction action) {
		
		entries.add(new BuiltinActionMenuEntry(action, keyBindings));
		
		return this;
	}
	
	List<MenuEntry> build() {
		return entries;
	}
}
