package dev.nimbler.ide.component.common.runner.model.throwable;

import java.util.Objects;

import com.sun.xml.txw2.annotation.XmlElement;

public class XmlStackTraceEntry {

    private String cl;
    
    private String method;
    
    private Integer lineNo;
    
    public XmlStackTraceEntry() {

    }

    public XmlStackTraceEntry(String cl, String method, Integer lineNo) {
        
        Objects.requireNonNull(cl);
        Objects.requireNonNull(method);
        Objects.requireNonNull(lineNo);
        
        this.cl = cl;
        this.method = method;
        this.lineNo = lineNo;
    }

    @XmlElement("class")
    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
    
    
    
}
