package dev.nimbler.ide.component.runners.model;

import java.nio.file.Path;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public class XmlSourceFileResourcePath {

    private XmlProject [] projects;
    
    private XmlSourceFolder sourceFolder;
    
    private String [] namespace;
    
    private String sourceFile;
    
    public XmlSourceFileResourcePath() {

    }
    
    public XmlSourceFileResourcePath(SourceFileResourcePath sourceFileResourcePath) {

        int numProjects = 0;
        
        for (
                ProjectModuleResourcePath projectPath = sourceFileResourcePath.getModule();
                projectPath != null;
                projectPath = (ProjectModuleResourcePath)projectPath.getParentPath()) {
            
            ++ numProjects;
        }
        
        this.projects = new XmlProject[numProjects];

        ProjectModuleResourcePath projectPath = sourceFileResourcePath.getModule();
        
        for (int i = 0; i < numProjects; ++ i) {

            projects[numProjects - i - 1] = new XmlProject(projectPath);
            
            projectPath = (ProjectModuleResourcePath)projectPath.getParentPath();
        }
        
        this.sourceFolder = new XmlSourceFolder(sourceFileResourcePath.getSourceFolderPath());
           
        this.namespace = sourceFileResourcePath.getNamespacePath().getNamespaceResource().getNamespace();
        
        this.sourceFile = sourceFileResourcePath.getName();
    }
    

    @XmlElementWrapper(name="projects")
    @XmlElement(name="project")
    public XmlProject [] getProjects() {
        return projects;
    }

    public void setProjects(XmlProject [] projects) {
        this.projects = projects;
    }

    public XmlSourceFolder getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(XmlSourceFolder sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    @XmlElementWrapper(name="namespace")
    @XmlElement(name="part")
    public String[] getNamespace() {
        return namespace;
    }

    public void setNamespace(String[] namespace) {
        this.namespace = namespace;
    }
    
    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public static class XmlProject {
        
        private String path;
        
        private String moduleId;
        
        public XmlProject() {
            
        }
        
        public XmlProject(ProjectModuleResourcePath resourcePath) {

            this.path = resourcePath.getFile().getAbsolutePath();
            this.moduleId = resourcePath.getModuleId().getId();  
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }
    }
    
    public static class XmlSourceFolder {

        private String path;
        
        private String name;

        private String language;
        
        public XmlSourceFolder() {
            
        }
        
        public XmlSourceFolder(SourceFolderResourcePath sourceFolderPath) {
            
            final Path modulePath = sourceFolderPath.getModule().getFile().toPath();
            
            this.path = modulePath.relativize(sourceFolderPath.getFile().toPath()).toString();

            this.name = sourceFolderPath.getSourceFolder().getName();

            this.language = sourceFolderPath.getSourceFolder().getLanguage().name();
            
        }
        
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}