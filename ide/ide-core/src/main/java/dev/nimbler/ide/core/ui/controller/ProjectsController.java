package dev.nimbler.ide.core.ui.controller;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.model.ProjectsModel;
import dev.nimbler.ide.core.ui.view.ProjectView;
import dev.nimbler.ide.core.ui.view.ProjectViewListener;

public final class ProjectsController {

	private final ProjectView view;

	public ProjectsController(ProjectsModel model, ProjectView view, EditUIController uiController) {

		Objects.requireNonNull(model);
		Objects.requireNonNull(view);
		
		this.view = view;

		view.addListener(new ProjectViewListenerImpl(uiController));
		
		view.addKeyEventListener(new ProjectViewKeyListener(view, uiController));
}
	
	void showInProjectView(SourceFileResourcePath sourceFile, boolean setFocusInProjectView) {
		
		Objects.requireNonNull(sourceFile);
		
		view.showSourceFile(sourceFile, setFocusInProjectView);
	}

	void refreshView() {
		view.refresh();
	}

	private static class ProjectViewListenerImpl implements ProjectViewListener {

		private final EditUIController uiController;
		
		ProjectViewListenerImpl(EditUIController uiController) {
			this.uiController = uiController;
		}

		@Override
		public void onSourceFileSelected(SourceFileResourcePath sourceFile) {
			uiController.openSourceFileForEditing(sourceFile);
		}
	}
}
