package dev.nimbler.ide.core.ui.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.SourceFileInfo;
import dev.nimbler.ide.common.codeaccess.SourceParseAccess;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.common.ui.controller.EditorActions;
import dev.nimbler.ide.common.ui.controller.EditorsListener;
import dev.nimbler.ide.component.common.language.LanguageComponent;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.core.ui.view.EditorSourceActionContextProvider;
import dev.nimbler.ide.core.ui.view.EditorView;
import dev.nimbler.ide.core.ui.view.EditorsView;
import dev.nimbler.ide.model.text.TextModel;
import dev.nimbler.ide.util.ui.text.styling.TextStylingModel;

final class EditorsController {

	private final EditorsView editorsView;
	private final TextEditorConfig config;
	private final SourceParseAccess sourceParseAccess;
	private final Languages languages;
	private final List<EditorsListener> listeners;
	
	private final Map<SourceFileResourcePath, EditorController> editorControllers;
	
	EditorsController(
	        EditorsView editorsView,
	        TextEditorConfig config,
	        SourceParseAccess sourceParseAccess,
	        Languages languages,
	        List<EditorsListener> listeners) {

		Objects.requireNonNull(editorsView);
		Objects.requireNonNull(config);
		Objects.requireNonNull(sourceParseAccess);
		Objects.requireNonNull(languages);
		
		this.editorsView = editorsView;
		this.config = config;
		this.sourceParseAccess = sourceParseAccess;
		this.languages = languages;
		this.listeners = listeners;
		
		this.editorControllers = new HashMap<>();
	}

	void closeFile(SourceFileResourcePath sourceFile) {
		editorsView.closeFile(sourceFile);
	}
	
	void closeCurrentEditedFile() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		if (currentEditedFile != null) {
			editorsView.closeFile(currentEditedFile);
		}
	}

	EditorActions getCurrentEditor() {
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		return currentEditedFile != null ? editorControllers.get(currentEditedFile) : null;
	}
	
	SourceFileResourcePath getCurrentEditedFile() {
		return editorsView.getCurrentEditedFile();
	}

	private static class EditorControllerDelegator implements EditorSourceActionContextProvider {
		private EditorController editorController;

		@Override
		public Collection<ActionContext> getActionContexts(long cursorOffset) {
		
			// Might be called while creating view so check for null
			return editorController != null
					? editorController.getActionContexts(cursorOffset)
					: null;
		}
	}
	
	EditorView displayFile(SourceFileInfo sourceFile, TextModel textModel) {

		final EditorControllerDelegator editorControllerDelegator = new EditorControllerDelegator();
		
		final DelegatingSourceFileModel delegatingSourceFileModel = new DelegatingSourceFileModel();
		
		final LanguageComponent languageComponent = languages.getLanguageComponent(sourceFile.getLanguage());

		final TextStylingModel textStylingModel = TextStylingHelper.makeTextStylingModel(languageComponent, delegatingSourceFileModel);
		
		final EditorView editorView = editorsView.displayFile(sourceFile.getPath(), textStylingModel, editorControllerDelegator);
		
		final EditorController editorController = new EditorController(
				editorView,
				config,
				listeners,
				textModel,
				sourceParseAccess,
				sourceFile,
				delegatingSourceFileModel);
		
		editorControllerDelegator.editorController = editorController;
		
		editorController.updateText();
		
		editorControllers.put(sourceFile.getPath(), editorController);
		
		editorView.addDisposeListener(() -> editorControllers.remove(sourceFile.getPath()));

		return editorView;
	}
}
