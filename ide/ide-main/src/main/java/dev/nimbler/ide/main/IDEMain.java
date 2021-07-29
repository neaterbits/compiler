package dev.nimbler.ide.main;

import java.io.File;

import org.eclipse.swt.widgets.Display;
import org.jutils.concurrency.scheduling.AsyncExecutor;
import org.jutils.swt.SWTAsyncExecutor;
import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.build.buildsystem.common.BuildSystems;
import dev.nimbler.build.buildsystem.common.ScanException;
import dev.nimbler.build.language.java.jdk.JavaRuntimeEnvironment;
import dev.nimbler.ide.code.CodeAccessImpl;
import dev.nimbler.ide.common.config.Configuration;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.component.application.runner.MainApplicationRunnerComponent;
import dev.nimbler.ide.component.build.ui.BuildIssuesComponent;
import dev.nimbler.ide.component.common.ConfigurationAccess;
import dev.nimbler.ide.component.common.IDERegisteredComponents;
import dev.nimbler.ide.component.compiledfiledebug.ui.CompiledFileViewComponent;
import dev.nimbler.ide.component.console.output.config.ConsoleConfiguration;
import dev.nimbler.ide.component.console.output.ui.ConsoleOutputComponent;
import dev.nimbler.ide.component.java.language.JavaLanguage;
import dev.nimbler.ide.component.java.language.JavaLanguageComponent;
import dev.nimbler.ide.component.java.ui.JavaUIComponentProvider;
import dev.nimbler.ide.component.runners.RunnersComponent;
import dev.nimbler.ide.component.runners.ui.RunnersComponentUI;
import dev.nimbler.ide.swt.SWTUI;
import dev.nimbler.ide.ui.controller.IDEController;
import dev.nimbler.ide.util.ui.text.LineDelimiter;

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

				final ForwardResultToCaller forward = runnable -> display.asyncExec(() -> {
					
					try {
						runnable.run();
					}
					catch (Throwable ex) {
						ex.printStackTrace();
					}
				});

				final BuildSystems buildSystems = new IDEBuildSystems(); 

				final IDERegisteredComponents ideComponents = registerComponents();
				
				final TextEditorConfig config = new TextEditorConfig(4, true);
				
				final CodeAccessImpl codeAccess = new CodeAccessImpl(
						asyncExecutor,
						forward,
						ideComponents.getLanguages(),
						buildSystems,
						new JavaLanguage());
				
				final IDEController ideController = new IDEController(
				        codeAccess,
				        ui,
				        config,
				        ideComponents,
				        new IDEMainTranslator(),
				        getConfigurationAccess());
				
				ideComponents.getTasksListeners().forEach(codeAccess::addTasksListener);
				
				// Run events on event queue before async jobs send event on event queue
				ui.runInitialEvents();
				
				try {
				    codeAccess.startIDEScanJobs(projectDir, new JavaRuntimeEnvironment());
				}
				catch (Exception ex) {
					System.err.println("Exception while staring scan jobs at " + ex.getMessage());
					
					ex.printStackTrace();
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
		
        // components.registerComponent(null, new TasksUIComponent());
		components.registerComponent(new JavaLanguageComponent(), new JavaUIComponentProvider());
        components.registerComponent(null, new BuildIssuesComponent());
        components.registerComponent(null, new CompiledFileViewComponent());

        components.registerComponent(null, new ConsoleOutputComponent());
		
		components.registerComponent(new RunnersComponent(), new RunnersComponentUI());
		components.registerComponent(new MainApplicationRunnerComponent(), null);
        
		return components;
	}
	
	private static ConfigurationAccess getConfigurationAccess() {
	    return new ConfigurationAccess() {
            
            @Override
            public <T extends Configuration> T getConfiguration(Class<T> type) {

                final Configuration configuration;
                
                if (type.equals(ConsoleConfiguration.class)) {
                    configuration = new ConsoleConfiguration(
                            LineDelimiter.getSystemLineDelimiter(),
                            1000000);
                }
                else {
                    configuration = null;
                }
                
                @SuppressWarnings("unchecked")
                final T t = (T)configuration;
                
                return t;
            }
        };
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
