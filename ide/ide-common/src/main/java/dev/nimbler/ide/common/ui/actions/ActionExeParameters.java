package dev.nimbler.ide.common.ui.actions;

import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.ide.common.codeaccess.CodeAccess;

public interface ActionExeParameters extends ActionSourceFileParameters {

    CodeAccess getCodeAccess();

    ForwardResultToCaller getForwardResultToCaller();
}
