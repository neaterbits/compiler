package dev.nimbler.ide.code.projects;

import java.io.File;

import org.junit.Test;
import org.mockito.Mockito;

import dev.nimbler.build.types.ModuleId;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.ide.common.codeaccess.ProjectsAccess.ProjectsListener;

public class ModuleScannerStateTest {

	@Test
	public void testReparenting() {
		
		final ProjectsListener listener = Mockito.mock(ProjectsListener.class);
		
		final ProjectsListeners listeners = new ProjectsListeners();
		
		listeners.addListener(listener);
		
		final ModuleScannerState state = new ModuleScannerState(listeners, Runnable::run);
		
		final ModuleId moduleId1 = new ModuleId("1");
		final ModuleId moduleId12 = new ModuleId("12");
		final ModuleId moduleId123 = new ModuleId("123");
		
		final File file = new File("/");
		
		state.onModuleScanned(moduleId123, file, moduleId12);
		
		final ModuleResource moduleResource1 = new ModuleResource(moduleId1, file);
		final ModuleResource moduleResource12 = new ModuleResource(moduleId12, file);
		final ModuleResource moduleResource123 = new ModuleResource(moduleId123, file);
		
		Mockito.verify(listener).onModuleAdded(new ProjectModuleResourcePath(moduleResource123));
		Mockito.verifyNoMoreInteractions(listener);
		Mockito.reset(listener);
		
		state.onModuleScanned(moduleId12, file, moduleId1);
		
		Mockito.verify(listener).onModuleAdded(new ProjectModuleResourcePath(moduleResource12));
		
		Mockito.verify(listener).onModuleRemoved(new ProjectModuleResourcePath(moduleResource123));
		Mockito.verify(listener).onModuleAdded(new ProjectModuleResourcePath(moduleResource12, moduleResource123));
		
		Mockito.verifyNoMoreInteractions(listener);
		Mockito.reset(listener);
		
		state.onModuleScanned(moduleId1, file, null);
		
		Mockito.verify(listener).onModuleAdded(new ProjectModuleResourcePath(moduleResource1));

		Mockito.verify(listener).onModuleRemoved(new ProjectModuleResourcePath(moduleResource12));
		Mockito.verify(listener).onModuleAdded(new ProjectModuleResourcePath(moduleResource1, moduleResource12));
		
		Mockito.verify(listener).onModuleRemoved(new ProjectModuleResourcePath(moduleResource12, moduleResource123));
		Mockito.verify(listener).onModuleAdded(new ProjectModuleResourcePath(moduleResource1, moduleResource12, moduleResource123));
		
		Mockito.verifyNoMoreInteractions(listener);
	}
}
