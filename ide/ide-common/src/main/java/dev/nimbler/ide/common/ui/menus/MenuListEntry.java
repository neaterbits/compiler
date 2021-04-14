package dev.nimbler.ide.common.ui.menus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class MenuListEntry extends TextMenuEntry {

	private final List<MenuEntry> entries;

	MenuListEntry(List<MenuEntry> entries) {

		Objects.requireNonNull(entries);
		
		this.entries = entries;
	}

	public final List<MenuEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public final void iterateItems(Consumer<MenuItemEntry<?, ?>> onMenuItem) {
		
		for (MenuEntry entry : entries) {
			if (entry instanceof MenuItemEntry) {
				onMenuItem.accept((MenuItemEntry<?, ?>)entry);
			}
			else if (entry instanceof MenuListEntry) {
				((MenuListEntry)entry).iterateItems(onMenuItem);
			}
			else if (entry instanceof SeparatorMenuEntry) {
				
			}
			else {
				throw new UnsupportedOperationException();
			}
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [entries=" + entries + "]";
	}
}
