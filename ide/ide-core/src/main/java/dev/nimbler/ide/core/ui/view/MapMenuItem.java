package dev.nimbler.ide.core.ui.view;

import java.util.function.BiFunction;

import dev.nimbler.ide.common.ui.menus.MenuItemEntry;

public interface MapMenuItem extends BiFunction<MenuItemEntry<?, ?>, ViewMenuItem, MenuSelectionListener> {

}
