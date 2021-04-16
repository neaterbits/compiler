package dev.nimbler.ide.core.ui.controller;

import java.util.Objects;

import dev.nimbler.build.types.resource.ResourcePath;
import dev.nimbler.ide.common.ui.keys.Key;
import dev.nimbler.ide.common.ui.keys.KeyLocation;
import dev.nimbler.ide.common.ui.keys.KeyMask;
import dev.nimbler.ide.common.ui.view.KeyEventListener;
import dev.nimbler.ide.core.ui.view.ProjectView;

final class ProjectViewKeyListener implements KeyEventListener {

	private final ProjectView projectView;
	private final EditUIController uiController;

	ProjectViewKeyListener(ProjectView projectView, EditUIController uiController) {
		
		Objects.requireNonNull(projectView);
		Objects.requireNonNull(uiController);

		this.projectView = projectView;
		this.uiController = uiController;
	}

	@Override
	public boolean onKeyPress(Key key, KeyMask mask, KeyLocation location) {

		if (key.getKeyCode() == 127) {
			
			final ResourcePath resourcePath = projectView.getSelected();
			
			if (resourcePath != null) {
				uiController.deleteResource(resourcePath);
			}
		}
		else if (key.getKeyCode() == Key.F5) {
			uiController.refreshProjectView();
		}
		
		return true;
	}

	@Override
	public boolean onKeyRelease(Key key, KeyMask mask, KeyLocation location) {
		
		return true;
	}
}
