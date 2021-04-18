package dev.nimbler.ide.code;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.util.PathUtil;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.util.concurrency.scheduling.AsyncExecutor;
import com.neaterbits.util.concurrency.scheduling.Constraint;
import com.neaterbits.util.concurrency.scheduling.ScheduleFunction;
import com.neaterbits.util.threads.ForwardResultToCaller;

import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.common.BuildSystems;
import dev.nimbler.build.buildsystem.common.ScanException;
import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.BuildRootImpl;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.code.codemap.CodeMapGatherer;
import dev.nimbler.ide.code.source.SourceFilesModel;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.codeaccess.SourceFileInfo;
import dev.nimbler.ide.common.model.codemap.CodeMapModel;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.scheduling.IDEScheduler;
import dev.nimbler.ide.common.scheduling.IDESchedulerImpl;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.core.tasks.InitialScanContext;
import dev.nimbler.ide.core.tasks.TargetBuilderIDEStartup;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public final class CodeAccessImpl implements CodeAccess {

	private final AsyncExecutor asyncExecutor;
	private final ForwardResultToCaller forwardResultToCaller;
	private final CodeMapGatherer codeMapGatherer;
	private final SourceFilesModel sourceFilesModel;
	private final CompileableLanguage language;

	private final BuildSystems buildSystems; 
	
	private final ProjectsListeners projectsListeners;
	private final ProjectsImpl projects;

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

		this.asyncExecutor = asyncExecutor;
		this.forwardResultToCaller = forwardResultToCaller;
		this.language = language;
		this.buildSystems = buildSystems;

		final CompilerCodeMap compilerCodeMap = new IntCompilerCodeMap();
		
		this.projectsListeners = new ProjectsListeners();
		this.projects = new ProjectsImpl(projectsListeners);
		
		this.codeMapGatherer = new CodeMapGatherer(
				asyncExecutor,
				language,
				projects,
				compilerCodeMap);

		final IDEScheduler ideScheduler = new IDESchedulerImpl(asyncExecutor);
		
		this.sourceFilesModel = new SourceFilesModel(ideScheduler, languages, codeMapGatherer, compilerCodeMap);
	}

	// ProjectsAccess
	
	@Override
	public CodeMapModel getCodeMapModel() {
		return codeMapGatherer;
	}

	/*
	@Override
	public Collection<ProjectModuleResourcePath> getModules() {
		return buildRoot.getModules();
	}
	*/

	@Override
	public List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {
		return projects.getSourceFolders(module);
	}

	@Override
	public void addProjectsListener(ProjectsListener listener) {

		projectsListeners.addListener(listener);
	}

	@Override
	public void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel) {
		
		sourceFilesModel.parseOnChange(sourceFile, text, codeMapGatherer, onUpdatedModel);
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
		
		scanBuildSystem(
				projectDir,
				runtimeEnvironment,
				this::scan);
	}
	
	private void scan(BuildRoot buildRoot) {
		
		final LogContext logContext = new LogContext();
	
		final TargetBuilderIDEStartup ideStartup = new TargetBuilderIDEStartup();
		final InitialScanContext context = new InitialScanContext(buildRoot, language, codeMapGatherer);
		
		ideStartup.execute(
		        logContext,
		        context,
                "sourcefolders",
		        new PrintlnTargetExecutorLogger(),
		        asyncExecutor,
		        null);
	}
	
	private void scanBuildSystem(File projectDir, RuntimeEnvironment runtimeEnvironment, Consumer<BuildRoot> onComplete) {
		
		final ScheduleFunction<Object, BuildRoot> function = obj -> {
			
			final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);

			final ModuleScannerState scannerState = new ModuleScannerState(projectsListeners, forwardResultToCaller);
			
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
		
		asyncExecutor.schedule(
				Constraint.IO,
				null,
				function,
				(param, buildRoot) -> {

					
					// Add build root on GUI thread
					forwardResultToCaller.forward(() -> {

						projects.addBuildRoot(buildRoot);
						
						if (onComplete != null) {
							onComplete.accept(buildRoot);
						}
					});
				});
		

	}

}
