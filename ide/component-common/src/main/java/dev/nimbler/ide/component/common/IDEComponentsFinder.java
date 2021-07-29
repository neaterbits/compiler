package dev.nimbler.ide.component.common;

import java.util.List;

import dev.nimbler.ide.component.common.ui.ComponentUI;

public interface IDEComponentsFinder {

    <T extends IDEComponent> List<T> findComponents(Class<T> type);

    <T extends ComponentUI> List<T> findComponentUIs(Class<T> type);
}
