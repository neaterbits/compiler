package com.neaterbits.ide.core.ui.view;

import java.util.function.BiFunction;

import com.neaterbits.ide.common.ui.menus.MenuItemEntry;

public interface MapMenuItem extends BiFunction<MenuItemEntry<?, ?>, ViewMenuItem, MenuSelectionListener> {

}
