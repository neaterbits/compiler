package dev.nimbler.ide.component.common.ui;

import dev.nimbler.ide.component.common.ComponentIDEAccess;

public interface CompositeComponentUI<UI_COMPONENT> extends ComponentUI {

    UI_COMPONENT addCompositeComponentUI(ComponentCompositeContext context, ComponentIDEAccess componentIDEAccess);
}
