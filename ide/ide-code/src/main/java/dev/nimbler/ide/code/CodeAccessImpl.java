package dev.nimbler.ide.code;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.util.PathUtil;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.util.concurrency.scheduling.AsyncExecutor;

import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.model.BuildRoot;
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
	private final BuildRoot buildRoot;
	private final CodeMapGatherer codeMapGatherer;
	private final SourceFilesModel sourceFilesModel;
	private final CompileableLanguage language;

	public CodeAccessImpl(
			AsyncExecutor asyncExecutor,
			Languages languages,
			BuildRoot buildRoot,
			CompileableLanguage language) {

		Objects.requireNonNull(asyncExecutor);
		Objects.requireNonNull(languages);
		Objects.requireNonNull(buildRoot);

		this.asyncExecutor = asyncExecutor;
		this.buildRoot = buildRoot;
		this.language = language;

		final CompilerCodeMap compilerCodeMap = new IntCompilerCodeMap();
		
		this.codeMapGatherer = new CodeMapGatherer(
				asyncExecutor,
				language,
				buildRoot,
				compilerCodeMap);

		final IDEScheduler ideScheduler = new IDESchedulerImpl(asyncExecutor);
		
		this.sourceFilesModel = new SourceFilesModel(ideScheduler, languages, codeMapGatherer, compilerCodeMap);
	}

	// ProjectsAccess
	
	@Override
	public CodeMapModel getCodeMapModel() {
		return codeMapGatherer;
	}

	@Override
	public Collection<ProjectModuleResourcePath> getModules() {
		return buildRoot.getModules();
	}

	@Override
	public List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {
		return buildRoot.getSourceFolders(module);
	}

	@Override
	public void addProjectsListener(ProjectsListener listener) {
		buildRoot.addListener(module -> listener.onSourceFoldersChanged(module));
	}

	@Override
	public void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel) {
		
		sourceFilesModel.parseOnChange(sourceFile, text, codeMapGatherer, onUpdatedModel);
	}

	@Override
	public SourceFolderResourcePath findSourceFolder(String projectName, String sourceFolder) {
		
		return buildRoot.forEachSourceFolder(folder -> {
			
			final String sourceFolderName = PathUtil.removeDirectoryFromPath(folder.getModule().getFile(), folder.getFile());
			
			return folder.getModule().getName().equals(projectName) && sourceFolderName.equals(sourceFolder)
					? folder
					: null;
		});
	}
	
	public void startIDEScanJobs() {

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
}
