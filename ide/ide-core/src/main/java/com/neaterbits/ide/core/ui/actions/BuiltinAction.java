package com.neaterbits.ide.core.ui.actions;

import com.neaterbits.ide.common.ui.translation.EnumTranslateable;
import com.neaterbits.ide.common.ui.translation.TranslationNamespaces;
import com.neaterbits.ide.core.ui.actions.types.clipboard.CopyAction;
import com.neaterbits.ide.core.ui.actions.types.clipboard.CutAction;
import com.neaterbits.ide.core.ui.actions.types.clipboard.PasteAction;
import com.neaterbits.ide.core.ui.actions.types.edit.CloseEditedAction;
import com.neaterbits.ide.core.ui.actions.types.edit.FindNextAction;
import com.neaterbits.ide.core.ui.actions.types.edit.FindPreviousAction;
import com.neaterbits.ide.core.ui.actions.types.edit.FindReplaceAction;
import com.neaterbits.ide.core.ui.actions.types.edit.MinMaxEditorsAction;
import com.neaterbits.ide.core.ui.actions.types.edit.RedoAction;
import com.neaterbits.ide.core.ui.actions.types.edit.SelectAllAction;
import com.neaterbits.ide.core.ui.actions.types.edit.UndoAction;
import com.neaterbits.ide.core.ui.actions.types.entity.DeleteAction;
import com.neaterbits.ide.core.ui.actions.types.entity.NewDialogAction;
import com.neaterbits.ide.core.ui.actions.types.entity.NewPopupAction;
import com.neaterbits.ide.core.ui.actions.types.navigate.OpenTypeAction;
import com.neaterbits.ide.core.ui.actions.types.navigate.ShowInProjectsAction;
import com.neaterbits.ide.core.ui.actions.types.source.CodeCompletionAction;
import com.neaterbits.ide.core.ui.actions.types.source.MoveAction;
import com.neaterbits.ide.core.ui.actions.types.source.RenameAction;
import com.neaterbits.ide.core.ui.actions.types.source.TypeHierarchyAction;

public enum BuiltinAction implements EnumTranslateable<BuiltinAction> {

	NEW_POPUP(NewPopupAction.class),
	NEW_DIALOG(NewDialogAction.class),
	DELETE(DeleteAction.class),
	
	UNDO(UndoAction.class),
	REDO(RedoAction.class),
	
	CUT(CutAction.class),
	PASTE(PasteAction.class),
	COPY(CopyAction.class),
	SELECT_ALL(SelectAllAction.class),
	
	FIND_REPLACE(FindReplaceAction.class),
	FIND_NEXT(FindNextAction.class),
	FIND_PREVIOUS(FindPreviousAction.class),
	
	OPEN_TYPE(OpenTypeAction.class),
	SHOW_IN_PROJECTS(ShowInProjectsAction.class),
		
	CLOSE_EDITED(CloseEditedAction.class),
	MIN_MAX_EDITORS(MinMaxEditorsAction.class),
	
	RENAME(RenameAction.class),
	MOVE(MoveAction.class),
	CODE_COMPLETION(CodeCompletionAction.class),
	
	TYPE_HIERARCHY(TypeHierarchyAction.class)
	;
	
	private final Class<? extends CoreAction> actionClass;
	private final CoreAction action;
	
	private BuiltinAction(Class<? extends CoreAction> actionClass) {
		this.actionClass = actionClass;
		try {
			this.action = actionClass.newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	public Class<? extends CoreAction> getActionClass() {
		return actionClass;
	}

	public CoreAction getAction() {
		return action;
	}

	@Override
	public String getTranslationNamespace() {
		return TranslationNamespaces.BUILTIN_ACTIONS;
	}

	@Override
	public String getTranslationId() {
		return getTranslationId(this);
	}
}
