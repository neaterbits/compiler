package dev.nimbler.ide.code.projects;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jutils.concurrency.scheduling.Constraint;
import org.jutils.concurrency.scheduling.ScheduleFunction;

import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.common.BuildSystems;
import dev.nimbler.build.buildsystem.common.ScanException;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.BuildRootImpl;
import dev.nimbler.build.model.BuildRoot.DependencySelector;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;
import dev.nimbler.ide.code.codemap.Projects;
import dev.nimbler.ide.code.tasks.TaskManager;
import dev.nimbler.ide.common.codeaccess.ProjectsAccess.ProjectsListener;
import dev.nimbler.ide.common.tasks.TaskName;

public final class ProjectsImpl implements Projects {
	
	private final TaskManager taskManager;
	
	private final ProjectsListeners projectsListeners;

	private final List<BuildRoot> buildRoots;
	
	public ProjectsImpl(TaskManager taskManager) {
		
		Objects.requireNonNull(taskManager);
		
		this.taskManager = taskManager;
		
		this.projectsListeners = new ProjectsListeners();
		
		this.buildRoots = new ArrayList<>();
	}

	void addBuildRoot(BuildRoot buildRoot) {
		
		Objects.requireNonNull(buildRoot);
		
		if (buildRoots.contains(buildRoot)) {
			throw new IllegalArgumentException();
		}
		
		buildRoots.add(buildRoot);
		
		buildRoot.addListener(projectsListeners.getBuildRootListener());
	}
	
	void removeBuildRoot(BuildRoot buildRoot) {

		Objects.requireNonNull(buildRoot);
		
		if (!buildRoots.contains(buildRoot)) {
			throw new IllegalArgumentException();
		}
		
		buildRoot.removeListener(projectsListeners.getBuildRootListener());
		
		buildRoots.remove(buildRoot);
	}
	
    public List<ProjectModuleResourcePath> getRootModules() {

        return buildRoots.stream().flatMap(root -> root.getModules().stream())
                .filter(ProjectModuleResourcePath::isAtRoot)
                .collect(Collectors.toList());
    }

	public void addListener(ProjectsListener listener) {
		projectsListeners.addListener(listener);
	}

	public void removeListener(ProjectsListener listener) {
		projectsListeners.removeListener(listener);
	}
	
	@Override
	public RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module) {

		return forEachBuildRoot(buildRoot -> buildRoot.getRuntimeEnvironment(module));
	}

	public TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module) {
	    return findBuildRoot(module).getTargetDirectory(module);
	}

    public List<ProjectDependency> getTransitiveProjectDependenciesForProjectModule(
            ProjectModuleResourcePath module,
            DependencySelector selector) {
        
        return findBuildRoot(module).getTransitiveProjectDependenciesForProjectModule(module, selector);
    }

    public List<LibraryDependency> getTransitiveLibraryDependenciesForProjectModule(
            ProjectModuleResourcePath module,
            DependencySelector selector) {
        
        return findBuildRoot(module).getTransitiveLibraryDependenciesForProjectModule(module, selector);
    }

	@Override
	public <T> T forEachSourceFolder(Function<SourceFolderResourcePath, T> function) {
		
		return forEachBuildRoot(buildRoot -> buildRoot.forEachSourceFolder(function));
	}
	
	public List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {

		final BuildRoot buildRoot = findBuildRoot(module);
		
		return buildRoot != null ? buildRoot.getSourceFolders(module) : Collections.emptyList();
	}
	
	public BuildRoot findBuildRoot(File rootDir) {
		
		Objects.requireNonNull(rootDir);
		
		return forEachBuildRoot(br -> br.getPath().equals(rootDir) ? br : null);
	}
	
	private BuildRoot findBuildRoot(ProjectModuleResourcePath module) {
		
		final BuildRoot buildRoot = forEachBuildRoot(br -> {
			
			final BuildRoot result;
			
			if (br.getModules().stream()
					.anyMatch(m -> m.equals(module))) {
				result = br;
			}
			else {
				result = null;
			}
			
			return result;
		});
		
		return buildRoot;
	}
	
	private <T> T forEachBuildRoot(Function<BuildRoot, T> function) {
		
		T result = null;
		
		for (BuildRoot buildRoot : buildRoots) {
			
			result = function.apply(buildRoot);
			
			if (result != null) {
				break;
			}
		}
		
		return result;
	}

	public TaskName scheduleBuildSystemScan(BuildSystems buildSystems, File projectDir, RuntimeEnvironment runtimeEnvironment) {
		
		final ScheduleFunction<Object, BuildRoot> function = obj -> {
			
			final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);

			final ModuleScannerState scannerState = new ModuleScannerState(projectsListeners, taskManager);
			
			BuildRoot buildRoot;

			try {
				buildRoot = new BuildRootImpl<>(
				                                    projectDir,
				                                    buildSystem.scan(projectDir, scannerState::onModuleScanned),
				                                    runtimeEnvironment);
			} catch (ScanException e) {

				buildRoot = null;
			}
			
			return buildRoot;
		};
		
		return taskManager.scheduleTask(
				"scan_" + projectDir.getPath(),
				"Scan for projects at '" + projectDir.getPath() + "'",
				Constraint.IO,
				null,
				function,
				(param, buildRoot) -> {

					// Called back on UI thread
					addBuildRoot(buildRoot);
				});
	}
}
