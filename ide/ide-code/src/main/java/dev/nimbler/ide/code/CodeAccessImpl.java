package dev.nimbler.ide.code;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.jutils.PathUtil;
import org.jutils.concurrency.scheduling.AsyncExecutor;
import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.build.buildsystem.common.BuildSystems;
import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.BuildRoot.DependencySelector;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;
import dev.nimbler.ide.code.codemap.CodeMapGatherer;
import dev.nimbler.ide.code.projects.ProjectsImpl;
import dev.nimbler.ide.code.source.SourceFilesModel;
import dev.nimbler.ide.code.tasks.TaskManagerImpl;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.codeaccess.SourceFileInfo;
import dev.nimbler.ide.common.model.codemap.CodeMapModel;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.tasks.TaskName;
import dev.nimbler.ide.common.tasks.TasksListener;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.core.tasks.InitialScanContext;
import dev.nimbler.ide.core.tasks.TargetBuilderIDEStartup;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public final class CodeAccessImpl implements CodeAccess {

	private final CodeMapGatherer codeMapGatherer;
	private final SourceFilesModel sourceFilesModel;
	private final CompileableLanguage language;

	private final BuildSystems buildSystems; 
	
	private final ProjectsImpl projects;
	
	private final TaskManagerImpl tasks;

	public CodeAccessImpl(
			AsyncExecutor asyncExecutor,
			ForwardResultToCaller forwardResultToCaller,
			Languages languages,
			BuildSystems buildSystems,
			CompileableLanguage language) {

		Objects.requireNonNull(asyncExecutor);
		Objects.requireNonNull(forwardResultToCaller);
		Objects.requireNonNull(languages);
		Objects.requireNonNull(buildSystems);

		this.language = language;
		this.buildSystems = buildSystems;

		final CompilerCodeMap compilerCodeMap = new IntCompilerCodeMap();
		
		this.tasks = new TaskManagerImpl(asyncExecutor, forwardResultToCaller);
		
		this.projects = new ProjectsImpl(tasks);
		
		this.codeMapGatherer = new CodeMapGatherer(
				asyncExecutor,
				language,
				projects,
				compilerCodeMap);

		this.sourceFilesModel = new SourceFilesModel(tasks, languages, codeMapGatherer, compilerCodeMap);
	}

	// ProjectsAccess
	
	@Override
	public CodeMapModel getCodeMapModel() {
		return codeMapGatherer;
	}

	@Override
    public List<ProjectModuleResourcePath> getRootModules() {
        return projects.getRootModules();
    }

    @Override
	public List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {
		return projects.getSourceFolders(module);
	}

	@Override
    public RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module) {
        return projects.getRuntimeEnvironment(module);
    }

    @Override
    public TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module) {
        return projects.getTargetDirectory(module);
    }

    @Override
    public List<ProjectDependency> getTransitiveProjectDependenciesForProjectModule(ProjectModuleResourcePath module,
            DependencySelector selector) {

        return projects.getTransitiveProjectDependenciesForProjectModule(module, selector);
    }

    @Override
    public List<LibraryDependency> getTransitiveLibraryDependenciesForProjectModule(ProjectModuleResourcePath module,
            DependencySelector selector) {

        return projects.getTransitiveLibraryDependenciesForProjectModule(module, selector);
    }

    @Override
	public void addProjectsListener(ProjectsListener listener) {

		projects.addListener(listener);
	}

	@Override
	public void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel) {
		
		sourceFilesModel.parseOnChange(sourceFile, text, codeMapGatherer, onUpdatedModel);
	}

	@Override
    public SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath) {
        return sourceFilesModel.getSourceFileModel(sourceFileResourcePath);
    }

    @Override
	public SourceFolderResourcePath findSourceFolder(String projectName, String sourceFolder) {
		
		return projects.forEachSourceFolder(folder -> {
			
			final String sourceFolderName = PathUtil.removeDirectoryFromPath(folder.getModule().getFile(), folder.getFile());
			
			return folder.getModule().getName().equals(projectName) && sourceFolderName.equals(sourceFolder)
					? folder
					: null;
		});
	}
	
	public void startIDEScanJobs(File projectDir, RuntimeEnvironment runtimeEnvironment) {
		
		final TaskName task = projects.scheduleBuildSystemScan(buildSystems, projectDir, runtimeEnvironment);
		
		try {
			scanSourceFolders(projectDir, task);
		}
		catch (Throwable ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	private void scanSourceFolders(File projectDir, TaskName buildSystemScan) {
		
		final TargetBuilderIDEStartup ideStartup = new TargetBuilderIDEStartup();
		
		tasks.scheduleTargets(
				"scan_source_folders_" + projectDir.getPath(),
				"Scan source folders at '" + projectDir.getPath() + "'", 
				ideStartup,
				"sourcefolders",
				() -> {
					final BuildRoot buildRoot = projects.findBuildRoot(projectDir);

					final InitialScanContext context = new InitialScanContext(buildRoot, language, codeMapGatherer);
					
					return context;
				},
				buildSystemScan);
	}
	
	@Override
	public void addTasksListener(TasksListener listener) {

		tasks.addListener(listener);
	}

	@Override
	public void triggerCompleteUpdate() {
		
		tasks.triggerCompleteUpdate();
	}
}
