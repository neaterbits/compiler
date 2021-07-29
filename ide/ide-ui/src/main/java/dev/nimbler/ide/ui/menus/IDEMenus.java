package dev.nimbler.ide.ui.menus;

import java.util.List;

import dev.nimbler.ide.common.ui.keys.KeyBindings;
import dev.nimbler.ide.common.ui.menus.BuiltinMenu;
import dev.nimbler.ide.common.ui.menus.Menus;
import dev.nimbler.ide.ui.actions.BuiltinAction;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.component.common.ui.MenuComponentUI;

public class IDEMenus {

	public static Menus makeMenues(KeyBindings keyBindings, IDEComponentsConstAccess componentsAccess) {
		
		final MenuBuilderImpl builder = new MenuBuilderImpl(keyBindings);
		
		builder
			.addSubMenu(BuiltinMenu.FILE, b -> b
					.addBuiltinAction(BuiltinAction.NEW_DIALOG)
			)
			.addSubMenu(BuiltinMenu.EDIT, b -> b
					.addBuiltinAction(BuiltinAction.UNDO)
					.addBuiltinAction(BuiltinAction.REDO)
					
					.addSeparator()
					
					.addBuiltinAction(BuiltinAction.CUT)
					.addBuiltinAction(BuiltinAction.COPY)
					.addBuiltinAction(BuiltinAction.PASTE)
			
					.addSeparator()
					
					.addBuiltinAction(BuiltinAction.DELETE)
					.addBuiltinAction(BuiltinAction.SELECT_ALL)
					
					.addSeparator()
					
					.addBuiltinAction(BuiltinAction.FIND_REPLACE)
					.addBuiltinAction(BuiltinAction.FIND_NEXT)
					.addBuiltinAction(BuiltinAction.FIND_PREVIOUS)
			)
			.addSubMenu(BuiltinMenu.REFACTOR,  b -> b
					.addBuiltinAction(BuiltinAction.RENAME)
					.addBuiltinAction(BuiltinAction.MOVE))
			.addSubMenu(BuiltinMenu.NAVIGATE, b -> b
					.addBuiltinAction(BuiltinAction.TYPE_HIERARCHY)
			)
			.addSubMenu(BuiltinMenu.RUN, b -> { });

		final List<MenuComponentUI> menuUIs = componentsAccess.findComponentUIs(MenuComponentUI.class);
		
		for (MenuComponentUI menuComponentUI : menuUIs) {
		    menuComponentUI.addToMenu(componentsAccess, builder);
		}

		return new Menus(builder.build());
	}
}
