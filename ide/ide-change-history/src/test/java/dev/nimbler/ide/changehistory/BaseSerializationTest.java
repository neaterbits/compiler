package dev.nimbler.ide.changehistory;

import java.nio.file.Path;

import org.jutils.PathUtil;

import dev.nimbler.build.types.ModuleId;
import dev.nimbler.build.types.language.Language;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResource;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResource;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public abstract class BaseSerializationTest {
    
    static Path ideBasePath() {
        return Path.of("ide", "base", "path");
    }
    
    static Path makeProjectModulePath(String ... parts) {
        
        
        return projectModulePath(Path.of(""))
                .resolve(PathUtil.pathFromParts(parts));
    }

    static Path makeProjectModulePath(Path ideBasePath, SourceFileResourcePath sourceFileResourcePath) {
        
        return ideBasePath
                    .relativize(sourceFileResourcePath.getFile().toPath());
    }

    static String makeProjectModulePathString(Path ideBasePath, SourceFileResourcePath sourceFileResourcePath) {
        return PathUtil.getForwardSlashPathString(makeProjectModulePath(ideBasePath, sourceFileResourcePath));
    }
    
    private static Path projectModulePath(Path ideBasePath) {

        return ideBasePath
                .resolve("project")
                .resolve("module");
    }

    private static String srcFolderName() {
        
        return "src";
    }

    private static Path srcFolderPath(Path ideBasePath) {
        
        return projectModulePath(ideBasePath).resolve(srcFolderName());
    }
    
    static SourceFileResourcePath makeSourceFileResourcePath(Path ideBasePath, String ... parts) {
        
        final Path projectModulePath = projectModulePath(ideBasePath);

        final Path filePath = projectModulePath.resolve(PathUtil.pathFromParts(parts));
        
        final ModuleResource moduleResource = new ModuleResource(new ModuleId("module"), projectModulePath.toFile());
    
        final ProjectModuleResourcePath moduleResourcePath = new ProjectModuleResourcePath(moduleResource);

        final Path srcPath = srcFolderPath(ideBasePath);

        final SourceFolderResource sourceFolderResource
            = new SourceFolderResource(srcPath.toFile(), srcFolderName(), Language.JAVA);
        
        final SourceFolderResourcePath sourceFolderResourcePath
            = new SourceFolderResourcePath(moduleResourcePath, sourceFolderResource);
        
        return new SourceFileResourcePath(
                sourceFolderResourcePath,
                new SourceFileResource(filePath.toFile()));
    }
}
