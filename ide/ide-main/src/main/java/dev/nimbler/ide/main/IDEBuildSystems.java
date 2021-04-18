package dev.nimbler.ide.main;

import java.io.File;

import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.common.BuildSystems;
import dev.nimbler.build.buildsystem.maven.MavenBuildSystem;

public class IDEBuildSystems implements BuildSystems {

	private final BuildSystem [] buildSystems;
	
	IDEBuildSystems() {
		this.buildSystems = new BuildSystem [] {
				
				new MavenBuildSystem()
				
		};
	}
	
	@Override
	public BuildSystem findBuildSystem(File projectDir) {
		
		for (BuildSystem buildSystem : buildSystems) {
			
			if (buildSystem.isBuildSystemFor(projectDir)) {
				return buildSystem;
			}
			
		}

		return null;
	}
	
}
