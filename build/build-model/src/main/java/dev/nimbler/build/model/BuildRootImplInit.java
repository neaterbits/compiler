package dev.nimbler.build.model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import dev.nimbler.build.buildsystem.common.BuildSystemRoot;
import dev.nimbler.build.types.ModuleId;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResource;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResource;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;

class BuildRootImplInit {

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	Map<MODULE_ID, ProjectModuleResourcePath> mapModuleIdToResourcePath(
				Map<MODULE_ID, PROJECT> projects,
				BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath = new HashMap<>(projects.size());

		for (PROJECT pom : projects.values()) {
			final LinkedList<ModuleResource> modules = new LinkedList<>();

			for (PROJECT project = pom; project != null; project = projects.get(buildSystemRoot.getParentModuleId(project))) {

				final ModuleResource moduleResource = new ModuleResource(
						buildSystemRoot.getModuleId(project),
						buildSystemRoot.getRootDirectory(project));

				modules.addFirst(moduleResource);
			}

			moduleIdToResourcePath.put(buildSystemRoot.getModuleId(pom), new ProjectModuleResourcePath(modules));
		}

		return moduleIdToResourcePath;
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	Map<ProjectModuleResourcePath, BuildProject<PROJECT>> makeBuildProjects(
			Map<MODULE_ID, PROJECT> projects,
			Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final Map<ProjectModuleResourcePath, BuildProject<PROJECT>> buildProjects = new HashMap<>();

		for (Map.Entry<MODULE_ID, PROJECT> entry : projects.entrySet()) {

			final MODULE_ID mavenModuleId = entry.getKey();

			final PROJECT pom = entry.getValue();

			// final List<BaseDependency> dependencies = findDependencies(pom, projects, moduleIdToResourcePath, buildSystemRoot);

			final BuildProject<PROJECT> buildProject = new BuildProject<>(pom /* , dependencies */);

			final ProjectModuleResourcePath moduleResourcePath = moduleIdToResourcePath.get(mavenModuleId);

			if (moduleResourcePath == null) {
				throw new IllegalStateException();
			}

			buildProjects.put(moduleResourcePath, buildProject);
		}

		return buildProjects;
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY> TargetDirectoryResourcePath getTargetDirectory(
			ProjectModuleResourcePath module,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final File targetDir = buildSystemRoot.getTargetDirectory(module.getFile());

		final TargetDirectoryResource targetDirectoryResource = new TargetDirectoryResource(targetDir);

		return new TargetDirectoryResourcePath(module, targetDirectoryResource);
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	CompiledModuleFileResourcePath getCompiledModuleFile(
	            ProjectModuleResourcePath module,
	            PROJECT project,
	            BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final File compiledModuleFile = buildSystemRoot.getCompiledModuleFile(project, module.getFile());

		return new CompiledModuleFileResourcePath(module, new CompiledModuleFileResource(compiledModuleFile));
	}
}
