package com.neaterbits.ide.component.common.instantiation;

import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.ui.ComponentDialogContext;
import com.neaterbits.ide.component.common.ui.ComponentUI;

public interface InstantiationComponentUI extends ComponentUI {

    void openNewableDialog(
            ComponentDialogContext uiContext,
            NewableCategoryName category,
            Newable newable,
            SourceFolderResourcePath sourceFolder,
            NamespaceResourcePath namespace,
            ComponentIDEAccess ideAccess);
}
