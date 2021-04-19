package dev.nimbler.ide.code.projects;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.build.types.ModuleId;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

final class ModuleScannerState {

	private final ProjectsListeners projectsListeners;
	private final ForwardResultToCaller forwardResultToCaller;
	
	private final Map<ModuleId, FoundModule> foundModules;
	
	ModuleScannerState(ProjectsListeners projectsListeners, ForwardResultToCaller forwardResultToCaller) {
		
		Objects.requireNonNull(projectsListeners);
		Objects.requireNonNull(forwardResultToCaller);
		
		this.projectsListeners = projectsListeners;
		this.forwardResultToCaller = forwardResultToCaller;

		this.foundModules = new HashMap<>();
	}

	void onModuleScanned(ModuleId moduleId, File moduleRoot, ModuleId parentModuleId) {

		if (foundModules.containsKey(moduleId)) {
			throw new IllegalStateException();
		}
		
		if (parentModuleId != null) {
			
			final FoundModule parentResourcePath = foundModules.get(parentModuleId);
			
			if (parentResourcePath != null) {

				final ProjectModuleResourcePath resourcePath = new ProjectModuleResourcePath(
																	parentResourcePath.path,
																	new ModuleResource(moduleId, moduleRoot));
				
				addModule(resourcePath, parentModuleId);
			}
			else {
				// Read before parent unless parent points to external module 
				addModule(moduleId, moduleRoot, parentModuleId);
			}
		}
		else {
			// Root project
			addModule(moduleId, moduleRoot, parentModuleId);
		}
	
		replaceReparented(moduleId);
	}
	
	private void replaceReparented(ModuleId moduleId) {
		
		final List<ModuleId> toReplaceModuleIds = new ArrayList<>();

		final List<ModuleId> triggerReplaceModuleIds = new ArrayList<>();

		triggerReplaceModuleIds.add(moduleId);
		
		int lastIterationSize = triggerReplaceModuleIds.size();
		
		for (;;) {
			// See if there are previous modules that now have parent module
			for (Map.Entry<ModuleId, FoundModule> entry : foundModules.entrySet()) {

				final ModuleId parentModuleId = entry.getValue().parent;
				final ModuleId entryModuleId = entry.getKey();

				if (
						   !toReplaceModuleIds.contains(entryModuleId)
						&& triggerReplaceModuleIds.contains(parentModuleId)) {
					
					toReplaceModuleIds.add(entryModuleId);
					
					triggerReplaceModuleIds.add(entryModuleId);
				}
			}
		
			// Exit if no more refs found
			if (lastIterationSize == triggerReplaceModuleIds.size()) {
				break;
			}
			
			lastIterationSize = triggerReplaceModuleIds.size();
		}
		
		// Now replace all found
		replace(toReplaceModuleIds, foundModules);
	}
	
	private void notifyOnModuleAdded(ProjectModuleResourcePath path) {
		
		Objects.requireNonNull(path);
		
		forwardResultToCaller.forward(() -> projectsListeners.onModuleAdded(path));
	}
	
	private void notifyOnModuleRemoved(ProjectModuleResourcePath path) {

		Objects.requireNonNull(path);
		
		forwardResultToCaller.forward(() -> projectsListeners.onModuleRemoved(path));
	}
	
	private void replace(List<ModuleId> toReplaceModuleIds, Map<ModuleId, FoundModule> foundModules) {
		
		for (ModuleId toReplace : toReplaceModuleIds) {

			final FoundModule foundModule = foundModules.get(toReplace);
			
			notifyOnModuleRemoved(foundModule.path);
			
			// Now add with full path
			
			final LinkedList<ModuleResource> modules = new LinkedList<>();
			
			for (
					FoundModule curModule = foundModule;
					curModule != null;
					curModule = curModule.parent != null ? foundModules.get(curModule.parent) : null) {
				
				modules.addFirst((ModuleResource)curModule.path.getLast());
			}

			final ProjectModuleResourcePath updated = new ProjectModuleResourcePath(modules);
			
			foundModules.put(toReplace, new FoundModule(updated, foundModule.parent));
			
			notifyOnModuleAdded(updated);
		}
	}
	
	private void addModule(ModuleId moduleId, File moduleRoot, ModuleId parentModuleId) {
		
		final ProjectModuleResourcePath resourcePath = makeResourcePath(moduleId, moduleRoot);
	
		addModule(resourcePath, parentModuleId);
	}
	
	private void addModule(ProjectModuleResourcePath resourcePath, ModuleId parentModuleId) {
		
		foundModules.put(resourcePath.getModuleId(), new FoundModule(resourcePath, parentModuleId));
		
		notifyOnModuleAdded(resourcePath);
	}
	
	private static ProjectModuleResourcePath makeResourcePath(ModuleId moduleId, File moduleRoot) {
		
		return new ProjectModuleResourcePath(Arrays.asList(new ModuleResource(moduleId, moduleRoot)));
	}
	
	private static class FoundModule {

		private final ProjectModuleResourcePath path;
		private final ModuleId parent;

		FoundModule(ProjectModuleResourcePath path, ModuleId parent) {
			this.path = path;
			this.parent = parent;
		}
	}

}
