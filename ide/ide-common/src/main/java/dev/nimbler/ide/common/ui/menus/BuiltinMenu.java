package dev.nimbler.ide.common.ui.menus;

import dev.nimbler.ide.common.ui.translation.EnumTranslateable;
import dev.nimbler.ide.common.ui.translation.TranslationNamespaces;

public enum BuiltinMenu implements EnumTranslateable<BuiltinMenu> {

	FILE,
	EDIT,
	SOURCE,
	REFACTOR,
	NAVIGATE,
	SEARCH,
	RUN;

	@Override
	public String getTranslationNamespace() {
		return TranslationNamespaces.MENUES;
	}

	@Override
	public String getTranslationId() {
		return getTranslationId(this);
	}
}
