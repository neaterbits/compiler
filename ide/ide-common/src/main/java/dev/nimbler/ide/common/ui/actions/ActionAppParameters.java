package dev.nimbler.ide.common.ui.actions;

import dev.nimbler.ide.common.codeaccess.CodeAccess;

public interface ActionAppParameters extends ActionSourceFileParameters {

    CodeAccess getCodeAccess();
}
