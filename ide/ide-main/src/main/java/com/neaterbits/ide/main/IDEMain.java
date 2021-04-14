package com.neaterbits.ide.main;

import java.io.File;

import org.eclipse.swt.widgets.Display;

import com.neaterbits.ide.common.scheduling.IDEScheduler;
import com.neaterbits.ide.common.scheduling.IDESchedulerImpl;
import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.common.language.CompileableLanguage;
import com.neaterbits.build.language.java.jdk.JavaRuntimeEnvironment;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.model.BuildRootImpl;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.component.build.ui.BuildIssuesComponent;
import com.neaterbits.ide.component.common.IDERegisteredComponents;
import com.neaterbits.ide.component.compiledfiledebug.ui.CompiledFileViewComponent;
import com.neaterbits.ide.component.java.language.JavaLanguage;
import com.neaterbits.ide.component.java.language.JavaLanguageComponent;
import com.neaterbits.ide.component.java.ui.JavaUIComponentProvider;
import com.neaterbits.ide.core.model.codemap.CodeMapGatherer;
import com.neaterbits.ide.core.source.SourceFilesModel;
import com.neaterbits.ide.core.tasks.InitialScanContext;
import com.neaterbits.ide.core.tasks.TargetBuilderIDEStartup;
import com.neaterbits.ide.core.ui.controller.IDEController;
import com.neaterbits.ide.swt.SWTUI;
import com.neaterbits.ide.util.swt.SWTAsyncExecutor;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.util.concurrency.scheduling.AsyncExecutor;

public class IDEMain {

	private static void usage() {
		System.err.println("usage: <projectdir>");
	}
	
	public static void main(String [] args) throws ScanException {
		
		try {
		if (args.length != 1) {
			usage();
		}
		else {
			final String projectDirString = args[0];
			
			final File projectDir = new File(projectDirString);
			
			if (!projectDir.isDirectory()) {
				usage();
			}
			else {
				
				final Display display = Display.getDefault();
				final AsyncExecutor asyncExecutor = new SWTAsyncExecutor(display);
				
				final SWTUI ui = new SWTUI(display);
				
				final BuildSystems buildSystems = new BuildSystems(); 

				final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);
				
				final BuildRoot buildRoot = new BuildRootImpl<>(
				                                    projectDir,
				                                    buildSystem.scan(projectDir),
				                                    new JavaRuntimeEnvironment());  
				
				final IDERegisteredComponents ideComponents = registerComponents();
				
				final TextEditorConfig config = new TextEditorConfig(4, true);
				
				final CompileableLanguage language = new JavaLanguage();

				final CompilerCodeMap compilerCodeMap = new IntCompilerCodeMap();
				
				final CodeMapGatherer codeMapGatherer = new CodeMapGatherer(
						asyncExecutor,
						language,
						buildRoot,
						compilerCodeMap);

				final IDEScheduler ideScheduler = new IDESchedulerImpl(asyncExecutor);
				
				final SourceFilesModel sourceFilesModel = new SourceFilesModel(
						ideScheduler,
						ideComponents.getLanguages(),
						codeMapGatherer,
						compilerCodeMap);
				
				final IDEController ideController = new IDEController(
				        buildRoot,
				        ui,
				        config,
				        ideComponents,
				        new IDEMainTranslator(),
				        sourceFilesModel,
				        codeMapGatherer.getModel());
				
				// Run events on event queue before async jobs send event on event queue
				ui.runInitialEvents();
				
				final LogContext logContext = new LogContext();
				
				try {
				    startIDEScanJobs(logContext, asyncExecutor, buildRoot, language, codeMapGatherer);
				}
				finally {
				    ui.main(ideController.getMainView());
				}
			}
		}
		}
		catch (Exception ex) {
			System.err.println("Exception at " + ex.getMessage());

			printStackTrace(ex.getStackTrace(), 5);
		}
	}
	
	private static IDERegisteredComponents registerComponents() {

		final IDERegisteredComponents components = new IDERegisteredComponents();
		
		components.registerComponent(new JavaLanguageComponent(), new JavaUIComponentProvider());
        components.registerComponent(null, new BuildIssuesComponent());
        components.registerComponent(null, new CompiledFileViewComponent());
		
		return components;
	}

	private static void startIDEScanJobs(
			LogContext logContext,
			AsyncExecutor asyncExecutor,
			BuildRoot buildRoot, 
			CompileableLanguage language,
			CodeMapGatherer codeMapGatherer) {
	
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
	
	private static void printStackTrace(StackTraceElement [] stackTrace, int num) {
		
		for (int i = 0; i < num; ++ i) {
			final StackTraceElement stackTraceElement = stackTrace[i];
			
			System.err.println("    "
			+ stackTraceElement.getClassName() + "."
			+ stackTraceElement.getMethodName() 
			+ ":" + stackTraceElement.getLineNumber());
		}
	}
}
