package dev.nimbler.ide.ui.controller;

import java.util.Objects;

import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.ui.controller.EditorsActions;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.view.UIDialogs;

final class ActionExecuteState {

	private final IDEComponentsConstAccess components;
	private final UIDialogs uiDialogs;
	private final Clipboard clipboard;
	private final UndoRedoBuffer undoRedoBuffer;
	private final ComponentIDEAccess componentIDEAccess;
	private final CodeAccess codeAccess;
	private final EditorsActions editorsActions;
	private FindReplaceDialogModel findReplaceModel;

	ActionExecuteState(
			IDEComponentsConstAccess components,
			UIDialogs uiDialogs,
			Clipboard clipboard,
			UndoRedoBuffer undoRedoBuffer,
			ComponentIDEAccess componentIDEAccess,
			CodeAccess codeAccess,
			EditorsActions editorsActions) {
	
		Objects.requireNonNull(components);
		Objects.requireNonNull(uiDialogs);
		Objects.requireNonNull(clipboard);
		Objects.requireNonNull(undoRedoBuffer);
		Objects.requireNonNull(componentIDEAccess);
		Objects.requireNonNull(codeAccess);
		Objects.requireNonNull(editorsActions);
		
		this.components = components;
		this.uiDialogs = uiDialogs;
		this.clipboard = clipboard;
		this.undoRedoBuffer = undoRedoBuffer;
		this.componentIDEAccess = componentIDEAccess;
		this.codeAccess = codeAccess;
		this.editorsActions = editorsActions;
	}

	IDEComponentsConstAccess getComponents() {
		return components;
	}

	UIDialogs getUIDialogs() {
		return uiDialogs;
	}

	Clipboard getClipboard() {
		return clipboard;
	}

	UndoRedoBuffer getUndoRedoBuffer() {
		return undoRedoBuffer;
	}

	ComponentIDEAccess getComponentIDEAccess() {
		return componentIDEAccess;
	}

	CodeAccess getCodeAccess() {
		return codeAccess;
	}

	EditorsActions getEditorsActions() {
		return editorsActions;
	}

	FindReplaceDialogModel getFindReplaceModel() {
		return findReplaceModel;
	}

	void setFindReplaceModel(FindReplaceDialogModel findReplaceModel) {
		
		Objects.requireNonNull(findReplaceModel);
		
		this.findReplaceModel = findReplaceModel;
	}
}
