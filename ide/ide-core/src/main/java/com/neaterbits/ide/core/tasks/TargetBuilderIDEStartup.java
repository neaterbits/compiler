package com.neaterbits.ide.core.tasks;

import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.TargetBuilder;

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
