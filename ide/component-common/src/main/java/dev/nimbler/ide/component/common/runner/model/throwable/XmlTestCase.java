package dev.nimbler.ide.component.common.runner.model.throwable;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class XmlTestCase {
    
    private String testClass;

    private List<String> testMehods;

    
    public XmlTestCase() {

    }

    public XmlTestCase(String testClass, List<String> testMehods) {
        this.testClass = testClass;
        this.testMehods = testMehods;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    @XmlElementWrapper(name="testMethods")
    @XmlElement(name="testMethod")
    public List<String> getTestMehods() {
        return testMehods;
    }

    public void setTestMehods(List<String> testMehods) {
        this.testMehods = testMehods;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((testClass == null) ? 0 : testClass.hashCode());
        result = prime * result + ((testMehods == null) ? 0 : testMehods.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        XmlTestCase other = (XmlTestCase) obj;
        if (testClass == null) {
            if (other.testClass != null)
                return false;
        } else if (!testClass.equals(other.testClass))
            return false;
        if (testMehods == null) {
            if (other.testMehods != null)
                return false;
        } else if (!testMehods.equals(other.testMehods))
            return false;
        return true;
    }
}
