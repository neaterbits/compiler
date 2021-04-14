package dev.nimbler.build.buildsystem.common;

import java.io.File;

public interface BuildSystems {
    
    BuildSystem findBuildSystem(File projectDir);

}
