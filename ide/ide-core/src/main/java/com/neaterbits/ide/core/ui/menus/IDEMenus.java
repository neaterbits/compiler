package com.neaterbits.ide.core.ui.menus;

import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.menus.BuiltinMenu;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.core.ui.actions.BuiltinAction;

public class IDEMenus {

	public static Menus makeMenues(KeyBindings keyBindings) {
		
		final MenuBuilder builder = new MenuBuilder(keyBindings);
		
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
			);

		return new Menus(builder.build());
	}
}
