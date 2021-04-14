package dev.nimbler.build.main;

import java.io.File;

import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.common.BuildSystems;
import dev.nimbler.build.buildsystem.maven.MavenBuildSystem;

public class AllBuildSystems implements BuildSystems {

	private final BuildSystem [] buildSystems;
	
	AllBuildSystems() {
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
