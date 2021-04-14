package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.common.ui.menus.MenuItemEntry;

public interface MenuSelectionListener {

	void onMenuItemSelected(MenuItemEntry<?, ?> menuItem);
	
}
