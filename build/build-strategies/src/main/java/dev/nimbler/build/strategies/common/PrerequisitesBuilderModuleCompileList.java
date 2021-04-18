package dev.nimbler.build.strategies.common;

import org.jutils.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import org.jutils.concurrency.scheduling.Constraint;

import dev.nimbler.build.types.compile.FileCompilation;
import dev.nimbler.build.types.compile.ModuleCompileList;
import dev.nimbler.build.types.compile.SourceFolderCompileList;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public final class PrerequisitesBuilderModuleCompileList extends PrerequisitesBuilderSpec<ModulesBuildContext, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<ModulesBuildContext, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("Module class compile list")
			.makingProduct(ModuleCompileList.class)
			.fromItemType(SourceFolderCompileList.class)
			
			.fromIterating(Constraint.IO, (context, module) -> context.getBuildRoot().getBuildSystemRootScan().findSourceFolders(module))
	
			.buildBy(st -> st
					
				.addInfoSubTarget(
						SourceFolderResourcePath.class,
                        "folder",
                        "collect",
						sourceFolder -> sourceFolder.getModule().getName(),
						sourceFolder -> "Class files for source folder " + sourceFolder.getName())
				
					.withPrerequisites("Source folder compilations")
						.makingProduct(SourceFolderCompileList.class)
						.fromItemType(FileCompilation.class)
						.fromIterating(Constraint.IO, (ctx, sourceFolder) -> SourceFilesBuilderUtil.getSourceFiles(ctx, sourceFolder))

						.buildBy(sourceFileTarget -> sourceFileTarget
							.addFileSubTarget(
							        FileCompilation.class,
							        "classfile",
							        "collect",
							        FileCompilation::getCompiledFile,
							        classFile -> "Class file for source file " + classFile.getSourceFile().getName())
								.withPrerequisite("Source file")
								.from(FileCompilation::getSourcePath)
								.withFile(SourceFileResourcePath::getFile)
						)
						.collectSubTargetsToProduct((sourceFolder, fileCompilationList) -> new SourceFolderCompileList(sourceFolder, fileCompilationList))
			)
			.collectSubProductsToProduct((module, resultList) -> new ModuleCompileList(module, resultList));

	}
}
