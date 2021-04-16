package dev.nimbler.ide.common.ui.actions;

public abstract class ActionException extends Exception {

    private static final long serialVersionUID = 1L;

    ActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
