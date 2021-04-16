package dev.nimbler.ide.component.runners.model;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.language.common.types.TypeName;

public final class RunConfiguration {

    private String name;

    private String runnerComponent;
   
    private SourceFileResourcePath sourceFile;
    
    private TypeName mainClass;
    
    private String programArguments;
    private String vmArguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getRunnerComponent() {
        return runnerComponent;
    }

    public void setRunnerComponent(String runnerComponent) {
        this.runnerComponent = runnerComponent;
    }

    @XmlTransient
    public ProjectModuleResourcePath getProject() {
        return sourceFile.getModule().getRoot();
    }

    @XmlJavaTypeAdapter(XmlSourceFileResourcePathAdapter.class)
    public SourceFileResourcePath getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(SourceFileResourcePath sourceFile) {
        this.sourceFile = sourceFile;
    }
    
    @XmlJavaTypeAdapter(XmlTypeNameAdapter.class)
    public TypeName getMainClass() {
        return mainClass;
    }

    public void setMainClass(TypeName mainClass) {
        this.mainClass = mainClass;
    }

    public String getProgramArguments() {
        return programArguments;
    }

    public void setProgramArguments(String programArguments) {
        this.programArguments = programArguments;
    }

    public String getVmArguments() {
        return vmArguments;
    }

    public void setVmArguments(String vmArguments) {
        this.vmArguments = vmArguments;
    }
}
