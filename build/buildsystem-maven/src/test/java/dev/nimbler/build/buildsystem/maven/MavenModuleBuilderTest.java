package dev.nimbler.build.buildsystem.maven;

import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.maven.common.model.MavenModuleId;
import dev.nimbler.build.test.BaseModuleBuilderTest;
import dev.nimbler.build.types.ModuleId;

public class MavenModuleBuilderTest extends BaseModuleBuilderTest {

	@Override
	protected String getJarFileName(String moduleName, ModuleId rootModuleId) {
		
		final MavenModuleId moduleId = (MavenModuleId)rootModuleId;
	
		return "buildsystem-common-" + moduleId.getVersion() + ".jar";
	}

	@Override
	protected BuildSystem makeBuildSystem() {
		return new MavenBuildSystem();
	}
}
