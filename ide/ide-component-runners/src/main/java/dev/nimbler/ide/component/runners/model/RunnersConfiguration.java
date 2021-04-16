package dev.nimbler.ide.component.runners.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="runners")
public class RunnersConfiguration {

    private List<RunConfiguration> runConfigurations;
    
    @XmlElementWrapper(name="runConfigurations")
    @XmlElement(name="runConfiguration")
    public List<RunConfiguration> getRunConfigurations() {
        return runConfigurations;
    }

    public void setRunConfigurations(List<RunConfiguration> runConfigurations) {
        this.runConfigurations = runConfigurations;
    }
}
