package com.neaterbits.ide.core.ui.keys;

import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.keys.QualifierKey;
import com.neaterbits.ide.core.ui.actions.BuiltinAction;

public class IDEKeyBindings {

	public static KeyBindings makeKeyBindings() {
	
		final KeyBindingsBuilder builder = new KeyBindingsBuilder();
		
		builder
			
			.addBuiltinAction(BuiltinAction.NEW_DIALOG, 'n', QualifierKey.CTRL)
			
			.addBuiltinAction(BuiltinAction.UNDO, 'z', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.REDO, 'z', QualifierKey.SHIFT, QualifierKey.CTRL)

			.addBuiltinAction(BuiltinAction.CUT, 'x', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.COPY, 'c', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.PASTE, 'v', QualifierKey.CTRL)

			.addBuiltinAction(BuiltinAction.SELECT_ALL, 'a', QualifierKey.CTRL)

			.addBuiltinAction(BuiltinAction.FIND_REPLACE, 'f', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.FIND_NEXT, 'k', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.FIND_PREVIOUS, 'k', QualifierKey.CTRL, QualifierKey.SHIFT)
			
			.addBuiltinAction(BuiltinAction.OPEN_TYPE, 't', QualifierKey.SHIFT, QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.SHOW_IN_PROJECTS, 'w', QualifierKey.SHIFT, QualifierKey.ALT)
			.addBuiltinAction(BuiltinAction.CLOSE_EDITED,  'w', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.MIN_MAX_EDITORS, 'm', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.TYPE_HIERARCHY, 't', QualifierKey.CTRL);
		
		return builder.build();
	}
	
}
