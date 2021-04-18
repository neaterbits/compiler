package dev.nimbler.ide.core.tasks;

import java.util.List;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.ActionResult;
import org.jutils.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import org.jutils.concurrency.scheduling.Constraint;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public class TargetBuilderGetSourceFolders extends TargetBuilderSpec<InitialScanContext> {

	public static final String NAME = "sourcefolders";
	
	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget(NAME, "source_folders", "scan_for_source_folders", "Source folders for all modules")
			.withPrerequisites("Source folders")
			.fromIterating(InitialScanContext::getModules)
			.buildBy(subTarget -> subTarget
					.addInfoSubTarget(
							ProjectModuleResourcePath.class,
							"modules_sourcefolders",
							"scan_modules_for_source_folders",
							ProjectModuleResourcePath::getName,
							module -> "Find source folders for " + module.getName())
					
					.actionWithResult(Constraint.IO, (context, module, params) ->  {

						final List<SourceFolderResourcePath> result = context.getBuildRoot().getBuildSystemRootScan().findSourceFolders(module);
						
						return new ActionResult<List<SourceFolderResourcePath>>(result, null);
					})
					.processResult((context, module, sourceFolders) -> context.getBuildRoot().setSourceFolders(module, sourceFolders))
			);
			
	}
}
