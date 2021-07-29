package dev.nimbler.ide.common.ui.menus;

import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.common.ui.translation.Translateable;

public final class SubMenuEntry extends MenuListEntry {

	private final Translateable name;
	
	public SubMenuEntry(Translateable name, List<MenuEntry> entries) {
		super(entries);
	
		Objects.requireNonNull(name);
		
		this.name = name;
	}

	public final Translateable getName() {
		return name;
	}

	@Override
	public final String getTranslationNamespace() {
		return name.getTranslationNamespace();
	}

	@Override
	public final String getTranslationId() {
		return name.getTranslationId();
	}
}
