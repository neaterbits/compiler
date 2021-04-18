package dev.nimbler.ide.core.tasks;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.TargetBuilder;

public final class TargetBuilderIDEStartup extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget(new TargetBuilderGetSourceFolders());
		
		targetBuilder.addTarget(new TargetBuilderProjectModulesCodeMap())
				
				// Run after getting source folders so that IDE opens faster
				// .withNamedPrerequisite(TargetBuilderGetSourceFolders.NAME)
				;
		
		// targetBuilder.addTarget(new TargetBuilderAddLibraryTypesToCodeMap());
		
		targetBuilder.addTarget(new TargetBuilderAddSystemLibraryTypeNamesToCodeMap());
		// targetBuilder.addTarget(new TargetBuilderAddSystemLibraryTypesToCodeMap());
	}
}
