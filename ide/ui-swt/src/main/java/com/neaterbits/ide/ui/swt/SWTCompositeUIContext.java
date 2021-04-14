package com.neaterbits.ide.ui.swt;

import java.util.Objects;

import org.eclipse.swt.widgets.Composite;

import com.neaterbits.ide.component.common.ui.ComponentCompositeContext;

public final class SWTCompositeUIContext extends ComponentCompositeContext {

    private final SWTViewList viewList;
    private final Composite composite;

    public SWTCompositeUIContext(SWTViewList viewList, Composite composite) {
        
        Objects.requireNonNull(viewList);
        Objects.requireNonNull(composite);
        
        this.viewList = viewList;
        this.composite = composite;
    }

    public SWTViewList getViewList() {
        return viewList;
    }

    public Composite getComposite() {
        return composite;
    }
}
