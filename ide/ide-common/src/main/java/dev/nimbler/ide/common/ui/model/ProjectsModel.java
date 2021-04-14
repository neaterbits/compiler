package dev.nimbler.ide.common.ui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import com.neaterbits.util.Files;

import dev.nimbler.build.common.tasks.util.SourceFileScanner;
import dev.nimbler.build.common.tasks.util.SourceFileScanner.Namespace;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.BuildRootListener;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.ResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResource;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public class ProjectsModel {

	private final BuildRoot buildRoot;
	
	private final List<ProjectModelListener> modelListeners;

	public ProjectsModel(BuildRoot buildRoot) {

		Objects.requireNonNull(buildRoot);

		this.buildRoot = buildRoot;
		
		this.modelListeners = new ArrayList<>();
		
		buildRoot.addListener(new BuildRootListener() {
			
			@Override
			public void onSourceFoldersChanged(ProjectModuleResourcePath module) {
				for (ProjectModelListener projectModelListener : modelListeners) {
					projectModelListener.onModelChanged();
				}
			}
		});
	}
	
	public void addListener(ProjectModelListener listener) {
		Objects.requireNonNull(listener);

		modelListeners.add(listener);
	}
	
	public ProjectModuleResourcePath getRoot() {
		return buildRoot.getModules().stream()
		.filter(module -> module.isAtRoot())
		.map(module -> new ProjectModuleResourcePath(new ModuleResource(((ModuleResource)module.getLast()).getModuleId(), module.getFile())))
		.findFirst()
		.get();
	}
	
	public List<ResourcePath> getResources(ResourcePath path) {
		
		Objects.requireNonNull(path);

		final List<ResourcePath> resources = new ArrayList<ResourcePath>();

		if (path.isAtRoot()) {
			findSubModulesAndFolders((ProjectModuleResourcePath)path, resources);
		}
		else if (path instanceof ProjectModuleResourcePath) {
			findSubModulesAndFolders((ProjectModuleResourcePath)path, resources);
		}
		else if (path instanceof SourceFolderResourcePath) {

			final SourceFolderResourcePath sourceFolderResourcePath = (SourceFolderResourcePath)path;

			final SourceFolderResource sourceFolderResource = sourceFolderResourcePath.getSourceFolder();

			if (sourceFolderResource.getLanguage() != null && sourceFolderResource.getLanguage().hasFolderNamespaces()) {
				findNamespaces(sourceFolderResourcePath, resources);
			}
			else {
				SourceFileScanner.findSourceFiles(sourceFolderResourcePath, sourceFolderResource.getFile(), resources);
			}
		}
		else if (path instanceof NamespaceResourcePath) {
			final NamespaceResourcePath namespaceResourcePath = (NamespaceResourcePath)path;
		
			SourceFileScanner.findSourceFiles(namespaceResourcePath, namespaceResourcePath.getFile(), resources);
		}
		else if (path instanceof SourceFileResourcePath) {
		
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return resources;
	}

	
	private void findSubModulesAndFolders(ProjectModuleResourcePath modulePath, List<ResourcePath> resources) {
		
		final Collection<ProjectModuleResourcePath> modules = buildRoot.getModules();
		
		for (ProjectModuleResourcePath module : modules) {
			
			if (module.isDirectSubModuleOf(modulePath)) {
				resources.add(module);
			}
		}
		
		final Collection<SourceFolderResourcePath> sourceFolders = buildRoot.getSourceFolders(modulePath);
	
		if (sourceFolders != null) {
			resources.addAll(sourceFolders);
		}
	}

	private void findNamespaces(SourceFolderResourcePath sourceFolderResourcePath, List<ResourcePath> resources) {
		
		final File sourceFolderFile = sourceFolderResourcePath.getFile();
		
		final List<File> files = new ArrayList<>();
		
		Files.recurseDirectories(sourceFolderFile, file -> {
			
			files.add(file);
			
		});
		
		final SortedMap<String, NamespaceResourcePath> sortedMap = new TreeMap<>();

		for (File file : files) {
			final Namespace namespace = SourceFileScanner.getNamespaceResource(sourceFolderFile, file);
			
			sortedMap.put(namespace.getDirPath(), new NamespaceResourcePath(sourceFolderResourcePath, namespace.getNamespace()));
		}

		for (Map.Entry<String, NamespaceResourcePath> entry : sortedMap.entrySet()) {
			resources.add(entry.getValue());
		}
	}
}
