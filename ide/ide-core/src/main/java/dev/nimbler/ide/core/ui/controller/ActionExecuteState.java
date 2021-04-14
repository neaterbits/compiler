package dev.nimbler.ide.core.ui.controller;

import java.util.Objects;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.model.codemap.CodeMapModel;
import dev.nimbler.ide.common.ui.controller.EditorsActions;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.core.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.core.ui.view.UIDialogs;

final class ActionExecuteState {

	private final IDEComponentsConstAccess components;
	private final UIDialogs uiDialogs;
	private final Clipboard clipboard;
	private final UndoRedoBuffer undoRedoBuffer;
	private final ComponentIDEAccess componentIDEAccess;
	private final BuildRoot buildRoot;
	private final EditorsActions editorsActions;
	private final CodeMapModel codeMap;
	private FindReplaceDialogModel findReplaceModel;

	ActionExecuteState(
			IDEComponentsConstAccess components,
			UIDialogs uiDialogs,
			Clipboard clipboard,
			UndoRedoBuffer undoRedoBuffer,
			ComponentIDEAccess componentIDEAccess,
			BuildRoot buildRoot,
			EditorsActions editorsActions,
			CodeMapModel codeMap) {
	
		Objects.requireNonNull(components);
		Objects.requireNonNull(uiDialogs);
		Objects.requireNonNull(clipboard);
		Objects.requireNonNull(undoRedoBuffer);
		Objects.requireNonNull(componentIDEAccess);
		Objects.requireNonNull(buildRoot);
		Objects.requireNonNull(editorsActions);
		Objects.requireNonNull(codeMap);
		
		this.components = components;
		this.uiDialogs = uiDialogs;
		this.clipboard = clipboard;
		this.undoRedoBuffer = undoRedoBuffer;
		this.componentIDEAccess = componentIDEAccess;
		this.buildRoot = buildRoot;
		this.editorsActions = editorsActions;
		this.codeMap = codeMap;
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

	BuildRoot getBuildRoot() {
		return buildRoot;
	}

	EditorsActions getEditorsActions() {
		return editorsActions;
	}

	CodeMapModel getCodeMap() {
		return codeMap;
	}

	FindReplaceDialogModel getFindReplaceModel() {
		return findReplaceModel;
	}

	void setFindReplaceModel(FindReplaceDialogModel findReplaceModel) {
		
		Objects.requireNonNull(findReplaceModel);
		
		this.findReplaceModel = findReplaceModel;
	}
}
