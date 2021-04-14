package com.neaterbits.ide.core.ui.controller;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.build.types.resource.ResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.codemap.CodeMapModel;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.controller.EditorActions;
import com.neaterbits.ide.common.ui.controller.EditorsActions;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.component.common.IDERegisteredComponents;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.core.source.SourceFileInfo;
import com.neaterbits.ide.core.source.SourceFilesModel;
import com.neaterbits.ide.core.ui.view.UIView;
import com.neaterbits.ide.core.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.model.text.StringTextModel;
import com.neaterbits.ide.model.text.UnixLineDelimiter;
import com.neaterbits.ide.util.IOUtil;
import com.neaterbits.util.PathUtil;

public final class EditUIController implements EditorsActions {

	private final UIView uiView;
	private final IDERegisteredComponents ideComponents;
	
	private final EditorsController editorsController;
	private final ProjectsController projectsController;
	private final CodeMapModel codeMapModel;
	
	EditUIController(
			UIViewAndSubViews uiView,
			TextEditorConfig config,
			ProjectsModel projectsModel,
			IDERegisteredComponents ideComponents,
			SourceFilesModel sourceFilesModel,
			CodeMapModel codeMapModel) {
		
		Objects.requireNonNull(uiView);
		Objects.requireNonNull(ideComponents);
		Objects.requireNonNull(codeMapModel);
		
		this.uiView = uiView;
		this.ideComponents = ideComponents;
		
		this.codeMapModel = codeMapModel;
		
		this.editorsController 	= new EditorsController(
				uiView.getEditorsView(),
				config,
				sourceFilesModel,
				ideComponents.getEditorsListeners());
		
		this.projectsController = new ProjectsController(projectsModel, uiView.getProjectView(), this);
	}

	EditorActions getCurrentEditor() {
		return editorsController.getCurrentEditor();
	}
	
	SourceFileResourcePath getCurrentEditedFile() {
		return editorsController.getCurrentEditedFile();
	}
	
	void refreshProjectView() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();
		
		if (!currentEditedFile.getFile().exists()) {
			closeFile(currentEditedFile);
		}
		
		projectsController.refreshView();
	}
	
	@Override
	public void openSourceFileForEditing(SourceFileResourcePath sourceFilePath) {
	
		Objects.requireNonNull(sourceFilePath);
		
		final LanguageName languageName = ideComponents.getLanguages().detectLanguage(sourceFilePath);
		
		String text = null;
		try {
			text = IOUtil.readAll(sourceFilePath.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (text != null) {
			final StringTextModel textModel = new StringTextModel(UnixLineDelimiter.INSTANCE, text);
			
			uiView.setWindowTitle(makeTitle(sourceFilePath));
			
			final SourceFileInfo sourceFile = new SourceFileInfo(
					sourceFilePath,
					ideComponents.getLanguages().getLanguageComponent(languageName),
					codeMapModel);
			
			
			editorsController.displayFile(sourceFile, textModel);
		}
	}
	
	void showInProjectView(SourceFileResourcePath sourceFile, boolean setFocusInProjectView) {
		
		projectsController.showInProjectView(sourceFile, setFocusInProjectView);
	}
	
	private static String makeTitle(SourceFileResourcePath sourceFile) {
		return sourceFile.getModule().getName()
			 + '/' + PathUtil.removeDirectoryFromPath(sourceFile.getModule().getFile(), sourceFile.getFile());
	}

	@Override
	public void showCurrentEditedInProjectView() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		if (currentEditedFile != null) {
			projectsController.showInProjectView(currentEditedFile, true);
		}
	}
	
	@Override
	public void closeCurrentEditedFile() {
		editorsController.closeCurrentEditedFile();
	}
	
	@Override
	public void minMaxEditors() {
		uiView.minMaxEditors();
	}

	void deleteResource(ResourcePath resourcePath) {

		// TODO do this based on change in project model instead
		if (Boolean.TRUE) {
			throw new UnsupportedOperationException();
		}
		
		Objects.requireNonNull(resourcePath);
		
		if (resourcePath instanceof SourceFileResourcePath) {
			final SourceFileResourcePath sourceFile = (SourceFileResourcePath)resourcePath;
		
			sourceFile.getFile().delete();
			
			final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

			if (sourceFile.equals(currentEditedFile)) {
				closeFile(sourceFile);
			}
		}

		projectsController.refreshView();
	}

	private void closeFile(SourceFileResourcePath sourceFile) {
		uiView.setWindowTitle("");
	}
}
