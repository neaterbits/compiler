package dev.nimbler.ide.component.build.ui;

import org.eclipse.swt.widgets.Control;

import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.ui.ComponentCompositeContext;
import dev.nimbler.ide.component.common.ui.DetailsComponentUI;
import dev.nimbler.ide.ui.swt.SWTCompositeUIContext;

public final class BuildIssuesComponent implements DetailsComponentUI<Control> {

    @Override
    public Control addCompositeComponentUI(ComponentCompositeContext context, ComponentIDEAccess componentIDEAccess) {

        final SWTCompositeUIContext uiContext = (SWTCompositeUIContext)context;
        
        return new SWTBuildIssuesView(uiContext.getViewList(), uiContext.getComposite()).getControl();
    }
}
