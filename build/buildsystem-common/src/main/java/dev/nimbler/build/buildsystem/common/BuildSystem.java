package dev.nimbler.build.buildsystem.common;

import java.io.File;

import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

import dev.nimbler.build.types.ModuleId;

public interface BuildSystem {

	boolean isBuildSystemFor(File rootDirectory);

	@FunctionalInterface
	public interface BuildSystemScanListener {
		
		void onModuleFound(ModuleId moduleId, File moduleRoot, ModuleId parentModuleId);
	}
	
	<MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> scan(
			File rootDirectory,
			BuildSystemScanListener onModuleFound) throws ScanException;

	<CONTEXT extends TaskContext> BuildSpecifier<CONTEXT> getBuildSpecifier();
}
