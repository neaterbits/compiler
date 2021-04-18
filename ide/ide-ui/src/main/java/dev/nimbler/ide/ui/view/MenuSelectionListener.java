package dev.nimbler.ide.ui.view;

import dev.nimbler.ide.common.ui.menus.MenuItemEntry;

public interface MenuSelectionListener {

	void onMenuItemSelected(MenuItemEntry<?, ?> menuItem);
	
}
