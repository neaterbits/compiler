package dev.nimbler.ide.component.common.runner.model.throwable;

import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

public final class XmlStackTrace {

    private List<XmlStackTraceEntry> entries;

    public XmlStackTrace() {

    }

    public XmlStackTrace(List<XmlStackTraceEntry> entries) {

        Objects.requireNonNull(entries);
        
        this.entries = entries;
    }

    @XmlElement(name="entry")
    public List<XmlStackTraceEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<XmlStackTraceEntry> entries) {
        this.entries = entries;
    }
}
