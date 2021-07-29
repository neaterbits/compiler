package dev.nimbler.ide.component.runners.model;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import dev.nimbler.build.types.ModuleId;
import dev.nimbler.build.types.language.Language;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.NamespaceResource;
import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResource;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResource;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.component.runners.model.XmlSourceFileResourcePath.XmlProject;

public class XmlSourceFileResourcePathAdapter
    extends XmlAdapter<XmlSourceFileResourcePath, dev.nimbler.build.types.resource.SourceFileResourcePath> {

    @Override
    public SourceFileResourcePath unmarshal(XmlSourceFileResourcePath v) throws Exception {
        
        ProjectModuleResourcePath projectModuleResourcePath = null;
        
        final List<ModuleResource> moduleResources = new ArrayList<>();
        
        Path path = null;
        
        for (XmlProject xmlProject : v.getProjects()) {
        
            path = path == null
                    ? Path.of(xmlProject.getPath())
                    : path.resolve(xmlProject.getPath());
            
            final ModuleId moduleId = new ModuleId(xmlProject.getModuleId());
            
            moduleResources.add(new ModuleResource(moduleId, path.toFile()));
            
            projectModuleResourcePath
                = new ProjectModuleResourcePath(new ArrayList<>(moduleResources));
        }
        
        final Path sourceFolderPath = path.resolve(v.getSourceFolder().getPath());
        
        final SourceFolderResourcePath sourceFolderResourcePath
            = new SourceFolderResourcePath(
                    projectModuleResourcePath,
                    new SourceFolderResource(
                            sourceFolderPath.toFile(),
                            v.getSourceFolder().getName(),
                            Language.valueOf(v.getSourceFolder().getLanguage())));
        
        Path namespacePath = sourceFolderPath;
        
        for (String namespacePart : v.getNamespace()) {
            namespacePath = namespacePath.resolve(namespacePart);
        }

        final NamespaceResourcePath namespaceResourcePath
            = new NamespaceResourcePath(
                    sourceFolderResourcePath,
                    new NamespaceResource(namespacePath.toFile(), v.getNamespace()));
        
        final SourceFileResourcePath sourceFileResourcePath
            = new SourceFileResourcePath(
                    namespaceResourcePath,
                    new SourceFileResource(new File(v.getSourceFile())));

        return sourceFileResourcePath;
    }

    @Override
    public XmlSourceFileResourcePath marshal(SourceFileResourcePath v) throws Exception {

        return new XmlSourceFileResourcePath(v);
    }
}
