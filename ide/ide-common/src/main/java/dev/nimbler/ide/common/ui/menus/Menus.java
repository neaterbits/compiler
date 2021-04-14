package dev.nimbler.ide.common.ui.menus;

import java.util.List;

public final class Menus extends MenuListEntry {

	public Menus(List<MenuEntry> entries) {
		super(entries);
	}

	@Override
	public String getTranslationNamespace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTranslationId() {
		throw new UnsupportedOperationException();
	}
}
