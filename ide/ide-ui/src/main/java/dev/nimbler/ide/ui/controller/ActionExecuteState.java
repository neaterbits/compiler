package dev.nimbler.ide.ui.controller;

import java.util.Objects;

import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.clipboard.Clipboard;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.codemap.CodeMapModel;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.ui.controller.EditorsActions;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.view.UIDialogs;

abstract class ActionExecuteState {

	private final IDEComponentsConstAccess components;
	private final UIDialogs uiDialogs;
	private final Clipboard clipboard;
	private final UndoRedoBuffer undoRedoBuffer;
	private final ComponentIDEAccess componentIDEAccess;
	private final CodeAccess codeAccess;
	private final EditorsActions editorsActions;
	private final ForwardResultToCaller forwardResultToCaller;
	
	private FindReplaceDialogModel findReplaceModel;

    abstract SourceFileResourcePath getCurrentSourceFileResourcePath();

    abstract SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath);

	ActionExecuteState(
			IDEComponentsConstAccess components,
			UIDialogs uiDialogs,
			Clipboard clipboard,
			UndoRedoBuffer undoRedoBuffer,
			ComponentIDEAccess componentIDEAccess,
			CodeAccess codeAccess,
			EditorsActions editorsActions,
			ForwardResultToCaller forwardResultToCaller) {
	
		Objects.requireNonNull(components);
		Objects.requireNonNull(uiDialogs);
		Objects.requireNonNull(clipboard);
		Objects.requireNonNull(undoRedoBuffer);
		Objects.requireNonNull(componentIDEAccess);
		Objects.requireNonNull(codeAccess);
		Objects.requireNonNull(editorsActions);
		Objects.requireNonNull(forwardResultToCaller);
		
		this.components = components;
		this.uiDialogs = uiDialogs;
		this.clipboard = clipboard;
		this.undoRedoBuffer = undoRedoBuffer;
		this.componentIDEAccess = componentIDEAccess;
		this.codeAccess = codeAccess;
		this.editorsActions = editorsActions;
		this.forwardResultToCaller = forwardResultToCaller;
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

	CodeMapModel getCodeMap() {
		return codeAccess.getCodeMapModel();
	}

	ForwardResultToCaller getForwardResultToCaller() {
        return forwardResultToCaller;
    }

    FindReplaceDialogModel getFindReplaceModel() {
		return findReplaceModel;
	}

	void setFindReplaceModel(FindReplaceDialogModel findReplaceModel) {
		
		Objects.requireNonNull(findReplaceModel);
		
		this.findReplaceModel = findReplaceModel;
	}
}
