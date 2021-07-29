package dev.nimbler.ide.component.runners.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import dev.nimbler.language.common.types.TypeName;

public class XmlTypeName {
    
    private String [] namespace;
    
    private String [] outerTypes;

    private String name;
    
    public XmlTypeName() {
        
    }

    public XmlTypeName(TypeName typeName) {
        
        this.namespace = typeName.getNamespace();
        this.outerTypes = typeName.getOuterTypes();
        this.name = typeName.getName();
    }
    
    @XmlElementWrapper(name="namespace")
    @XmlElement(name="part")
    public String[] getNamespace() {
        return namespace;
    }

    public void setNamespace(String[] namespace) {
        this.namespace = namespace;
    }

    @XmlElementWrapper(name="outerTypes")
    @XmlElement(name="type")
    public String [] getOuterTypes() {
        return outerTypes;
    }

    public void setOuterTypes(String [] outerTypes) {
        this.outerTypes = outerTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}