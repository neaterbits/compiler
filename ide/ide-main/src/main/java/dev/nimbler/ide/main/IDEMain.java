package dev.nimbler.ide.main;

import java.io.File;

import org.eclipse.swt.widgets.Display;

import com.neaterbits.ide.util.swt.SWTAsyncExecutor;
import com.neaterbits.util.concurrency.scheduling.AsyncExecutor;

import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.common.ScanException;
import dev.nimbler.build.language.java.jdk.JavaRuntimeEnvironment;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.BuildRootImpl;
import dev.nimbler.ide.code.CodeAccessImpl;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.component.build.ui.BuildIssuesComponent;
import dev.nimbler.ide.component.common.IDERegisteredComponents;
import dev.nimbler.ide.component.compiledfiledebug.ui.CompiledFileViewComponent;
import dev.nimbler.ide.component.java.language.JavaLanguage;
import dev.nimbler.ide.component.java.language.JavaLanguageComponent;
import dev.nimbler.ide.component.java.ui.JavaUIComponentProvider;
import dev.nimbler.ide.swt.SWTUI;
import dev.nimbler.ide.ui.controller.IDEController;

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
				
				final CodeAccessImpl codeAccess = new CodeAccessImpl(
						asyncExecutor,
						ideComponents.getLanguages(),
						buildRoot,
						new JavaLanguage());
				
				final IDEController ideController = new IDEController(
				        codeAccess,
				        ui,
				        config,
				        ideComponents,
				        new IDEMainTranslator());
				
				// Run events on event queue before async jobs send event on event queue
				ui.runInitialEvents();
				
				try {
				    codeAccess.startIDEScanJobs();
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
