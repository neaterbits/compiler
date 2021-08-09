package dev.nimbler.ide.common.paths;

import java.nio.file.Path;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public class IDEPaths {

    private static final String IDE_DIR = ".ide";

    private static Path getHomeDir() {
        
        return Path.of(System.getProperty("user.home"));
    }
    
    /*
    public static Path getRootConfigDir() {

        return getIDETempBasePath();
    }
    */

    public static Path getIDETempBasePath() {
        
        return getHomeDir().resolve(IDE_DIR);
    }

    private static Path getProjectTempDir(ProjectModuleResourcePath module) {

        if (!module.isAtRoot()) {
            throw new IllegalArgumentException();
        }

        final Path modulePath = module.getFile().toPath().toAbsolutePath();

        final Path homeDir = getHomeDir();
        
        if (!modulePath.startsWith(homeDir)) {
            throw new IllegalArgumentException();
        }

        final Path subtract = homeDir.relativize(modulePath);
        
        return getIDETempBasePath().resolve(subtract);
    }

    public static Path getProjectConfigDir(ProjectModuleResourcePath module) {
        
        return getProjectTempDir(module).resolve("config");
    }
    
    public static Path getProjectUndoRedoDir(ProjectModuleResourcePath module) {
        
        return getProjectTempDir(module).resolve("undoredo");
    }
}
