package dev.nimbler.ide.component.common.runner.model.throwable;

import java.util.Objects;

public class XmlThrowable {

    private String type;

    private String message;
    
    private XmlStackTrace stackTrace;
    
    private XmlThrowable cause;

    public XmlThrowable() {

    }

    public XmlThrowable(Class<?> type, String message, XmlStackTrace stackTrace, XmlThrowable cause) {
        
        Objects.requireNonNull(type);
        Objects.requireNonNull(stackTrace);
        
        this.type = type.getName();
        this.message = message;
        this.stackTrace = stackTrace;
        this.cause = cause;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public XmlStackTrace getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(XmlStackTrace stackTrace) {
        this.stackTrace = stackTrace;
    }

    public XmlThrowable getCause() {
        return cause;
    }

    public void setCause(XmlThrowable cause) {
        this.cause = cause;
    }
}
