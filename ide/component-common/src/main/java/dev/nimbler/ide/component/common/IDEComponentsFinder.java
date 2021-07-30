package dev.nimbler.ide.component.common;

import java.util.List;

import dev.nimbler.ide.component.common.ui.ComponentUI;

public interface IDEComponentsFinder {

    <T extends IDEComponent> List<T> findComponents(Class<T> type);

    <T extends ComponentUI> List<T> findComponentUIs(Class<T> type);

    default <T extends ComponentUI> T findComponentUI(Class<T> type) {
        
        final List<T> uis = findComponentUIs(type);
        
        if (uis.size() != 1) {
            throw new IllegalStateException();
        }

        return uis.get(0);
    }
}
