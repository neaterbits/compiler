package com.neaterbits.ide.component.common.ui;

public interface CompositeComponentUI<UI_COMPONENT> extends ComponentUI {

    UI_COMPONENT addCompositeComponentUI(ComponentCompositeContext context);
}
