package dev.nimbler.build.common.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.common.tasks.util.SourceFileScanner;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;

class BuildUtil {

	static FilesToCompile getFilesToCompile(
			ProjectModuleResourcePath module,
			Collection<SourceFolderResourcePath> sourceFolders,
			BuildRoot buildRoot,
			CompileableLanguage language) {
		
		
		final List<SourceFileResourcePath> allModuleSourceFiles = new ArrayList<>();
		
		for (SourceFolderResourcePath sourceFolder : sourceFolders) {
			
			SourceFileScanner.findSourceFiles(sourceFolder, allModuleSourceFiles);
		}
		
		final TargetDirectoryResourcePath targetDirectory = buildRoot.getTargetDirectory(module);
		
		final List<SourceFileResourcePath> toCompile = new ArrayList<>(allModuleSourceFiles.size());
		final List<SourceFileResourcePath> alreadyBuilt = new ArrayList<>(allModuleSourceFiles.size());
		
		for (SourceFileResourcePath sourceFile : allModuleSourceFiles) {
			final CompiledFileResourcePath compiledFilePath = language.getCompiledFilePath(targetDirectory, sourceFile);
		
			final File file = compiledFilePath.getFile();
			
			if (!file.exists() || file.lastModified() <= sourceFile.getFile().lastModified()) {
				toCompile.add(sourceFile);
			}
			else {
				alreadyBuilt.add(sourceFile);
			}
		}
		
		return new FilesToCompile(toCompile, alreadyBuilt);
	}
}
