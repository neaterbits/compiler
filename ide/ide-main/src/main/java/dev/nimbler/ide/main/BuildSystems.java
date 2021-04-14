package dev.nimbler.ide.main;

import java.io.File;

import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.maven.MavenBuildSystem;

public class BuildSystems {

	private final BuildSystem [] buildSystems;
	
	BuildSystems() {
		this.buildSystems = new BuildSystem [] {
				
				new MavenBuildSystem()
				
		};
	}
	
	BuildSystem findBuildSystem(File projectDir) {
		
		for (BuildSystem buildSystem : buildSystems) {
			
			if (buildSystem.isBuildSystemFor(projectDir)) {
				return buildSystem;
			}
			
		}

		return null;
	}
	
}
