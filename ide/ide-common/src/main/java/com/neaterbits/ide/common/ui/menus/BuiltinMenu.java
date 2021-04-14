package com.neaterbits.ide.common.ui.menus;

import com.neaterbits.ide.common.ui.translation.EnumTranslateable;
import com.neaterbits.ide.common.ui.translation.TranslationNamespaces;

public enum BuiltinMenu implements EnumTranslateable<BuiltinMenu> {

	FILE,
	EDIT,
	SOURCE,
	REFACTOR,
	NAVIGATE,
	SEARCH;

	@Override
	public String getTranslationNamespace() {
		return TranslationNamespaces.MENUES;
	}

	@Override
	public String getTranslationId() {
		return getTranslationId(this);
	}
}
