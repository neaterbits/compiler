package dev.nimbler.build.strategies.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.build.common.tasks.util.SourceFileScanner;
import dev.nimbler.build.types.compile.FileCompilation;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;

public class SourceFilesBuilderUtil {

	public static List<FileCompilation> getSourceFiles(TaskBuilderContext context, SourceFolderResourcePath sourceFolder) {
		
		final List<SourceFileResourcePath> sourceFiles = new ArrayList<>();

		SourceFileScanner.findSourceFiles(sourceFolder, sourceFiles);

		final TargetDirectoryResourcePath targetDirectory = context.getBuildRoot().getTargetDirectory(sourceFolder.getModule());

		return sourceFiles.stream()
				.map(sourceFile -> new FileCompilation(sourceFile, context.getLanguage().getCompiledFilePath(targetDirectory, sourceFile)))
				.collect(Collectors.toList());
	}
}
