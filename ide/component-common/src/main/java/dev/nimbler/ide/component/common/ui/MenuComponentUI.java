package dev.nimbler.ide.component.common.ui;

import dev.nimbler.ide.common.ui.menus.MenuBuilder;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;

public interface MenuComponentUI extends ComponentUI {

    void addToMenu(IDEComponentsConstAccess componentsAccess, MenuBuilder menuBuilder);
}
