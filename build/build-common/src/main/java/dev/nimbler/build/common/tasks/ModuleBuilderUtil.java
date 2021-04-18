package dev.nimbler.build.common.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public class ModuleBuilderUtil {
	
	public static List<ProjectDependency> transitiveProjectDependencies(BuildRoot buildRoot, ProjectModuleResourcePath module) {

		Objects.requireNonNull(buildRoot);
		Objects.requireNonNull(module);
		
	    return transitiveProjectDependencies(
	            module,
	            buildRoot::getDirectProjectDependenciesForProjectModule,
	            ProjectDependency::getModulePath);
	}

	public static <PROJECT, DEPENDENCY>
	List<DEPENDENCY> transitiveProjectDependencies(
	        PROJECT module,
	        Function<PROJECT, Collection<DEPENDENCY>> getDependencies,
	        Function<DEPENDENCY, PROJECT> getProject) {
		
		Objects.requireNonNull(module);
		Objects.requireNonNull(getDependencies);
		Objects.requireNonNull(getProject);
		
		final LinkedHashSet<DEPENDENCY> dependencies = new LinkedHashSet<>();
		
		transitiveProjectDependencies(module, dependencies, getDependencies, getProject);

		return new ArrayList<>(dependencies);
	}

	private static <PROJECT, DEPENDENCY>
	void transitiveProjectDependencies(
	        PROJECT module,
	        LinkedHashSet<DEPENDENCY> dependencies,
	        Function<PROJECT, Collection<DEPENDENCY>> getDependencies,
	        Function<DEPENDENCY, PROJECT> getProject) {
	    
		final Collection<DEPENDENCY> moduleDependencies = getDependencies.apply(module);
		 
		dependencies.addAll(moduleDependencies);
		
		for (DEPENDENCY dependency : moduleDependencies) {
	        
		    final PROJECT sub = getProject.apply(dependency);

		    if (sub != null) {

		        transitiveProjectDependencies(
    			        sub,
    			        dependencies,
    			        getDependencies,
    			        getProject);
		    }
		}
	}
}
