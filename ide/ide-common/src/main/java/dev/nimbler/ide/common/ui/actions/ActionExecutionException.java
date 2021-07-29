package dev.nimbler.ide.common.ui.actions;

public final class ActionExecutionException extends ActionException {

    private static final long serialVersionUID = 1L;

    public ActionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
