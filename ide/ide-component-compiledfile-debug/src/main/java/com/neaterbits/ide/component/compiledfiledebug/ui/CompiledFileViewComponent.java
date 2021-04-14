package com.neaterbits.ide.component.compiledfiledebug.ui;

import org.eclipse.swt.widgets.Control;

import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.component.common.editors.EditorsComponent;
import com.neaterbits.ide.component.common.ui.ComponentCompositeContext;
import com.neaterbits.ide.component.common.ui.DetailsComponentUI;
import com.neaterbits.ide.ui.swt.SWTCompositeUIContext;

public final class CompiledFileViewComponent
    implements DetailsComponentUI<Control>, EditorsComponent {
    
    private SWTCompiledFileView compiledFileView;

    @Override
    public Control addCompositeComponentUI(ComponentCompositeContext context) {
        
        final SWTCompositeUIContext swtuiContext = (SWTCompositeUIContext)context;
        
        return new SWTCompiledFileView(swtuiContext.getComposite()).getControl();
    }

    @Override
    public void onSourceFileModelChanged(SourceFileModel model) {
        compiledFileView.onSourceFileModelChanged(model);
    }

    @Override
    public void onEditorCursorPosUpdate(long cursorOffset) {
        compiledFileView.onEditorCursorPosUpdate(cursorOffset);
    }
}
