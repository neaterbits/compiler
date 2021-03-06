package dev.nimbler.build.buildsystem.common;

import java.io.File;
import java.util.Collection;

import dev.nimbler.build.types.ModuleId;

public interface BuildSystemRoot<
            MODULE_ID extends ModuleId,
            PROJECT,
            DEPENDENCY,
            REPOSITORY> extends BuildSystemRootScan {

	Collection<PROJECT> getProjects();

	default PROJECT getProject(DEPENDENCY dependency) {
	    
	    final MODULE_ID moduleId = getDependencyModuleId(dependency);
	    
	    return getProjects().stream()
	            .filter(p -> getModuleId(p).equals(moduleId))
	            .findFirst()
	            .orElse(null);
	}

	default boolean isProjectModule(PROJECT project) {
	    
	    return isProjectModule(getModuleId(project));
	}

	default boolean isProjectModule(MODULE_ID moduleId) {
	    
	    return getProjects().stream()
	            .anyMatch(p -> getModuleId(p).equals(moduleId));
	}
	
	MODULE_ID getModuleId(PROJECT project);

	MODULE_ID getParentModuleId(PROJECT project);

	File getRootDirectory(PROJECT project);
	
	default PROJECT getProject(File rootDirectory) {

	    return getProjects().stream()
	            .filter(project -> rootDirectory.equals(getRootDirectory(project)))
	            .findFirst()
	            .orElse(null);
	}

	String getNonScopedName(PROJECT project);

	String getDisplayName(PROJECT project);

	Scope getDependencyScope(DEPENDENCY dependency);

	boolean isOptionalDependency(DEPENDENCY dependency);

	Collection<DEPENDENCY> getDirectDependenciesOfProject(PROJECT project);

	Collection<DEPENDENCY> getDirectDependenciesOfExternal(PROJECT external);

	PROJECT getExternalModule(DEPENDENCY dependency);

	MODULE_ID getDependencyModuleId(DEPENDENCY dependency);

	File repositoryJarFile(DEPENDENCY dependency);

	File repositoryJarFile(MODULE_ID moduleId, String classifier);

	String compiledFileName(DEPENDENCY dependency);

	void addListener(BuildSystemRootListener listener);

	File getTargetDirectory(File modulePath);

	File getCompiledModuleFile(PROJECT project, File modulePath);
}
