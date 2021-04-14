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

	public Translateable getName() {
		return name;
	}

	@Override
	public String getTranslationNamespace() {
		return name.getTranslationNamespace();
	}

	@Override
	public String getTranslationId() {
		return name.getTranslationId();
	}
}
