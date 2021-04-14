package com.neaterbits.ide.core.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.ui.model.ProjectsModel;

public final class UIModels {

	private final ProjectsModel projectsModel;

	public UIModels(ProjectsModel projectsModel) {

		Objects.requireNonNull(projectsModel);
		
		this.projectsModel = projectsModel;
	}

	public ProjectsModel getProjectsModel() {
		return projectsModel;
	}
}
