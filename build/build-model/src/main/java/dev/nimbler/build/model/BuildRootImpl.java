package dev.nimbler.build.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import dev.nimbler.build.buildsystem.common.BuildSystemRoot;
import dev.nimbler.build.buildsystem.common.BuildSystemRootScan;
import dev.nimbler.build.buildsystem.common.ScanException;
import dev.nimbler.build.buildsystem.common.Scope;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.ModuleId;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.dependencies.ModuleDependency;
import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.LibraryResource;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.ModuleResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResource;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;

public class BuildRootImpl<MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY> implements BuildRoot {

	private final File path;
	private final BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot;
	private final RuntimeEnvironment runtimeEnvironment;

	private final Map<ProjectModuleResourcePath, BuildProject<PROJECT>> projects;
	private final Map<MODULE_ID, PROJECT> buildSystemProjectByModuleId;
	private final Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath;

	private final List<BuildRootListener> listeners;

	public BuildRootImpl(
	        File path,
	        BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot,
	        RuntimeEnvironment runtimeEnvironment) {

		Objects.requireNonNull(path);

		this.path = path;

		this.buildSystemRoot = buildSystemRoot;
		this.runtimeEnvironment = runtimeEnvironment;
		
		this.listeners = new ArrayList<>();

		final Collection<PROJECT> projects = buildSystemRoot.getProjects();

		this.buildSystemProjectByModuleId = projects.stream()
				.collect(Collectors.toMap(buildSystemRoot::getModuleId, project -> project));

		this.moduleIdToResourcePath = BuildRootImplInit.mapModuleIdToResourcePath(buildSystemProjectByModuleId, buildSystemRoot);

		this.projects = BuildRootImplInit.makeBuildProjects(buildSystemProjectByModuleId, moduleIdToResourcePath, buildSystemRoot);
	}

	@Override
	public File getPath() {
		return path;
	}

	@Override
	public Collection<ProjectModuleResourcePath> getModules() {
		return Collections.unmodifiableCollection(moduleIdToResourcePath.values());
	}

	@Override
	public void setSourceFolders(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders) {

		projects.get(module).setSourceFolders(sourceFolders);

		for (BuildRootListener buildRootListener : listeners) {
			buildRootListener.onSourceFoldersChanged(module);
		}
	}

	@Override
	public List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {

		final BuildProject<PROJECT> buildProject = projects.get(module);

		if (buildProject == null) {
			throw new IllegalArgumentException("No buildproject with name " + module);
		}

		final List<SourceFolderResourcePath> sourceFolders = buildProject.getSourceFolders();
		
		return sourceFolders != null ? sourceFolders : Collections.emptyList();
	}

	@Override
	public List<ProjectDependency> getDirectProjectDependenciesForProjectModule(ProjectModuleResourcePath module) {

	    Objects.requireNonNull(module);
	    
	    final PROJECT root = buildSystemRoot.getProject(module.getFile());

	    if (!buildSystemRoot.isProjectModule(root)) {
	    	throw new IllegalArgumentException();
	    }
	    
	    final Collection<DEPENDENCY> dependencies = buildSystemRoot.getDirectDependenciesOfProject(root);
	    
	    final List<ProjectDependency> result = new ArrayList<>(dependencies.size());
	    
	    for (DEPENDENCY dep : dependencies) {
	    	
	    	final MODULE_ID depModuleId = buildSystemRoot.getDependencyModuleId(dep);
	    	
	    	if (buildSystemRoot.isProjectModule(depModuleId)) {

	    		final ProjectDependency projectDependency = makeProjectDependency(dep, depModuleId, module);
	    		
		    	result.add(projectDependency);
	    	}
	    }

	    return result;
	}

	@Override
	public List<ProjectDependency> getTransitiveProjectDependenciesForProjectModule(
			ProjectModuleResourcePath project,
			DependencySelector selector) {
		
		final LinkedHashSet<ProjectDependency> deps = new LinkedHashSet<>();
		
		getTransitiveProjectDependenciesForProjectModule(project, deps, selector);
		
		return new ArrayList<>(deps);
	}

	private void getTransitiveProjectDependenciesForProjectModule(
			ProjectModuleResourcePath project,
			LinkedHashSet<ProjectDependency> dst,
			DependencySelector selector) {
		
		final PROJECT root = buildSystemRoot.getProject(project.getFile());
		
		final Collection<DEPENDENCY> deps = buildSystemRoot.getDirectDependenciesOfProject(root);

	    for (DEPENDENCY dep : deps) {
	    	
	    	final MODULE_ID depModuleId = buildSystemRoot.getDependencyModuleId(dep);
	    	
	    	if (buildSystemRoot.isProjectModule(depModuleId)) {

	    		final ProjectDependency projectDependency = makeProjectDependency(dep, depModuleId, project);
	    		
		    	dst.add(projectDependency);
		    	
		    	getTransitiveProjectDependenciesForProjectModule(projectDependency.getModulePath(), dst, selector);
	    	}
	    }
	}

	private ProjectDependency makeProjectDependency(DEPENDENCY dep, MODULE_ID depModuleId, ProjectModuleResourcePath modulePath) {
		
		final PROJECT depProject = buildSystemRoot.getProject(dep);
		
		final File depDirectory = buildSystemRoot.getRootDirectory(depProject);
		
		final ProjectModuleResourcePath path = new ProjectModuleResourcePath(
				modulePath,
				new ModuleResource(depModuleId, depDirectory));
		
		final File compiledModuleFile = buildSystemRoot.getCompiledModuleFile(depProject, path.getFile());
		
		final ProjectDependency projectDependency = new ProjectDependencyImpl<>(path, dep, compiledModuleFile);
		
		return projectDependency;
	}
	
	@Override
	public List<LibraryDependency> getDirectLibraryDependenciesForProjectModule(ProjectModuleResourcePath module,
			DependencySelector selector) {

	    Objects.requireNonNull(module);
	    
	    final PROJECT root = buildSystemRoot.getProject(module.getFile());

	    if (!buildSystemRoot.isProjectModule(root)) {
	    	throw new IllegalArgumentException();
	    }
	    
	    final Collection<DEPENDENCY> dependencies = buildSystemRoot.getDirectDependenciesOfProject(root);
	    
	    final List<LibraryDependency> result = new ArrayList<>(dependencies.size());
	    
	    for (DEPENDENCY dep : dependencies) {
	    	
	    	final MODULE_ID depModuleId = buildSystemRoot.getDependencyModuleId(dep);
	    	
	    	if (!buildSystemRoot.isProjectModule(depModuleId)) {
		    	result.add(makeLibraryDependency(dep));
	    	}
	    }

	    return result;
	}
	
	@Override
	public List<LibraryDependency> getTransitiveLibraryDependenciesForProjectModules(
			Collection<ProjectModuleResourcePath> modules, DependencySelector selector) {
		
		final LinkedHashSet<LibraryDependency> deps = new LinkedHashSet<>();
		
		for (ProjectModuleResourcePath path : modules) {
			
			final PROJECT project = buildSystemRoot.getProject(path.getFile());
			
			transitiveModuleDependencies(
					project,
					deps,
					module -> buildSystemRoot.getDirectDependenciesOfExternal(module),
					selector,
					dep -> buildSystemRoot.getDependencyScope(dep),
					dep -> buildSystemRoot.isOptionalDependency(dep),
					dep -> buildSystemRoot.getExternalModule(dep),
					this::makeLibraryDependency);
			
		}
		
		return new ArrayList<>(deps);
	}

	private LibraryDependency makeLibraryDependency(DEPENDENCY dep) {
		
		final PROJECT depModule = buildSystemRoot.getExternalModule(dep);
		
		final File depDirectory = buildSystemRoot.getRootDirectory(depModule);
		
		final LibraryResourcePath path = new LibraryResourcePath(
				new LibraryResource(depDirectory));
		
		final File compiledModuleFile = buildSystemRoot.getCompiledModuleFile(depModule, path.getFile());

		final LibraryDependency libraryDependency = new LibraryDependencyImpl<>(path, dep, compiledModuleFile);
		
		return libraryDependency;
	}

	@Override
	public List<LibraryDependency> getTransitiveDependenciesForExternalLibrary(
			LibraryDependency dependency,
			DependencySelector selector) {

		@SuppressWarnings("unchecked")
		final LibraryDependencyImpl<DEPENDENCY> impl = (LibraryDependencyImpl<DEPENDENCY>)dependency;
		
		final PROJECT externalModule = buildSystemRoot.getExternalModule(impl.getBuildSystemDependency());
		
		final List<LibraryDependency> deps = transitiveModuleDependencies(
				externalModule,
				module -> buildSystemRoot.getDirectDependenciesOfExternal(module),
				selector,
				dep -> buildSystemRoot.getDependencyScope(dep),
				dep -> buildSystemRoot.isOptionalDependency(dep),
				dep -> buildSystemRoot.getExternalModule(dep),
				this::makeLibraryDependency);
		
		return deps;
	}

	@Override
	public TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module) {
		return BuildRootImplInit.getTargetDirectory(module, buildSystemRoot);
	}

	@Override
	public void downloadExternalDependencyAndAddToBuildModel(ProjectModuleResourcePath referencedFrom, LibraryDependency dependency)
	            throws IOException, ScanException {

	    /* TODO
		final LibraryDependencyImpl impl = (LibraryDependencyImpl)dependency;

		@SuppressWarnings("unchecked")
		final BuildDependency<DEPENDENCY> buildDependency = (BuildDependency<DEPENDENCY>)impl.getDependency();

		buildSystemRoot.downloadExternalDependencyIfNotPresentAndAddToModel(
		        null, // TODO pass repositories buildSystemRoot.getProject(referencedFrom.getFile()),
		        buildDependency.getDependency());
	    */
	}

	@Override
	public CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module) {

		return BuildRootImplInit.getCompiledModuleFile(module, projects.get(module).getBuildSystemProject(), buildSystemRoot);
	}

	@Override
	public void addListener(BuildRootListener listener) {

		Objects.requireNonNull(listener);

		listeners.add(listener);
	}

	@Override
	public void removeListener(BuildRootListener listener) {

		Objects.requireNonNull(listener);

		listeners.remove(listener);
	}

	@Override
	public BuildSystemRootScan getBuildSystemRootScan() {
		return buildSystemRoot;
	}

    @Override
    public RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module) {
        return runtimeEnvironment;
    }


	private static <MODULE, DEPENDENCY, MAPPED>
	List<MAPPED> transitiveModuleDependencies(
	        MODULE module,
	        Function<MODULE, Collection<DEPENDENCY>> getDependencies,
	        DependencySelector dependencySelector,
	        Function<DEPENDENCY, Scope> getScope,
	        Predicate<DEPENDENCY> isOptional,
	        Function<DEPENDENCY, MODULE> getModule,
	        Function<DEPENDENCY, MAPPED> map) {
		
		Objects.requireNonNull(module);
		Objects.requireNonNull(dependencySelector);
		Objects.requireNonNull(getDependencies);
		Objects.requireNonNull(getScope);
		Objects.requireNonNull(isOptional);
		Objects.requireNonNull(getModule);
		Objects.requireNonNull(map);
		
		final LinkedHashSet<MAPPED> dependencies = new LinkedHashSet<>();
		
		transitiveModuleDependencies(
				module,
				dependencies,
				getDependencies,
				dependencySelector,
				getScope,
				isOptional,
				getModule,
				map);

		return new ArrayList<>(dependencies);
	}

	private static <MODULE, DEPENDENCY, MAPPED>
	void transitiveModuleDependencies(
	        MODULE module,
	        LinkedHashSet<MAPPED> dependencies,
	        Function<MODULE, Collection<DEPENDENCY>> getDependencies,
	        DependencySelector dependencySelector,
	        Function<DEPENDENCY, Scope> getScope,
	        Predicate<DEPENDENCY> isOptional,
	        Function<DEPENDENCY, MODULE> getModule,
	        Function<DEPENDENCY, MAPPED> map) {
	    
		final Collection<DEPENDENCY> moduleDependencies = getDependencies.apply(module);
		 
		for (DEPENDENCY dependency : moduleDependencies) {
			
			final Scope scope = getScope.apply(dependency);
			final boolean optional = isOptional.test(dependency);
			
			if (dependencySelector.include(scope, optional)) {
				
				dependencies.add(map.apply(dependency));
	        
			    final MODULE sub = getModule.apply(dependency);
	
			    if (sub != null) {
	
			        transitiveModuleDependencies(
	    			        sub,
	    			        dependencies,
	    			        getDependencies,
	    			        dependencySelector,
	    			        getScope,
	    			        isOptional,
	    			        getModule,
	    			        map);
			    }
			}
		}
	}
	
	private static abstract class BaseDependency<MODULE_RESOURCE_PATH extends ModuleResourcePath, DEPENDENCY>
		implements ModuleDependency<MODULE_RESOURCE_PATH> {

		private final MODULE_RESOURCE_PATH moduleResourcePath;
		private final DEPENDENCY buildSystemDependency;
		private final File compiledModuleFile;
		
		BaseDependency(
				MODULE_RESOURCE_PATH resourcePath,
				DEPENDENCY buildSystemDependency,
				File compiledModuleFile) {
			
			Objects.requireNonNull(resourcePath);
			Objects.requireNonNull(buildSystemDependency);
			
			this.moduleResourcePath = resourcePath;
			this.buildSystemDependency = buildSystemDependency;
			this.compiledModuleFile = compiledModuleFile;
		}

		public final MODULE_RESOURCE_PATH getModulePath() {
			return moduleResourcePath;
		}

		@Override
		public CompiledModuleFileResourcePath getCompiledModuleFilePath() {
			
			return new CompiledModuleFileResourcePath(
					this.moduleResourcePath,
					new CompiledModuleFileResource(compiledModuleFile));
		}

		public final DEPENDENCY getBuildSystemDependency() {
			return buildSystemDependency;
		}

		@Override
		public File getCompiledModuleFile() {
			return compiledModuleFile;
		}

		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((moduleResourcePath == null) ? 0 : moduleResourcePath.hashCode());
			return result;
		}

		@Override
		public final boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final BaseDependency<?, ?> other = (BaseDependency<?, ?>) obj;
			if (moduleResourcePath == null) {
				if (other.moduleResourcePath != null)
					return false;
			} else if (!moduleResourcePath.equals(other.moduleResourcePath))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return moduleResourcePath != null ? moduleResourcePath.getName() : "<null>";
		}
	}

	private static class ProjectDependencyImpl<DEPENDENCY>
			extends BaseDependency<ProjectModuleResourcePath, DEPENDENCY>
			implements ProjectDependency {

		ProjectDependencyImpl(
				ProjectModuleResourcePath resourcePath,
				DEPENDENCY buildSystemDependency,
				File compiledModuleFile) {

			super(resourcePath, buildSystemDependency, compiledModuleFile);
		}
	}
	
	private static class LibraryDependencyImpl<DEPENDENCY>
		extends BaseDependency<LibraryResourcePath, DEPENDENCY>
		implements LibraryDependency {

		LibraryDependencyImpl(
				LibraryResourcePath resourcePath,
				DEPENDENCY buildSystemDependency,
				File compiledModuleFile) {
			
			super(resourcePath, buildSystemDependency, compiledModuleFile);
		}
	}
}
