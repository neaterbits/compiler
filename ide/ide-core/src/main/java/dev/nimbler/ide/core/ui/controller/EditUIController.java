package dev.nimbler.ide.core.ui.controller;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.util.PathUtil;

import dev.nimbler.build.types.resource.ResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.SourceFileInfo;
import dev.nimbler.ide.common.codeaccess.SourceParseAccess;
import dev.nimbler.ide.common.codeaccess.types.LanguageName;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.common.ui.controller.EditorActions;
import dev.nimbler.ide.common.ui.controller.EditorsActions;
import dev.nimbler.ide.common.ui.model.ProjectsModel;
import dev.nimbler.ide.component.common.IDERegisteredComponents;
import dev.nimbler.ide.core.ui.view.UIView;
import dev.nimbler.ide.core.ui.view.UIViewAndSubViews;
import dev.nimbler.ide.model.text.StringTextModel;
import dev.nimbler.ide.model.text.UnixLineDelimiter;
import dev.nimbler.ide.util.IOUtil;

public final class EditUIController implements EditorsActions {

	private final UIView uiView;
	private final IDERegisteredComponents ideComponents;
	
	private final EditorsController editorsController;
	private final ProjectsController projectsController;
	// private final CodeMapModel codeMapModel;
	
	EditUIController(
			UIViewAndSubViews uiView,
			TextEditorConfig config,
			ProjectsModel projectsModel,
			IDERegisteredComponents ideComponents,
			SourceParseAccess sourceParseAccess /*,
			CodeMapModel codeMapModel */) {
		
		Objects.requireNonNull(uiView);
		Objects.requireNonNull(ideComponents);
		// Objects.requireNonNull(codeMapModel);
		
		this.uiView = uiView;
		this.ideComponents = ideComponents;
		
		// this.codeMapModel = codeMapModel;
		
		this.editorsController 	= new EditorsController(
				uiView.getEditorsView(),
				config,
				sourceParseAccess,
				ideComponents.getLanguages(),
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
					languageName /*,
					ideComponents.getLanguages().getLanguageComponent(languageName),
					codeMapModel */);
			
			
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
