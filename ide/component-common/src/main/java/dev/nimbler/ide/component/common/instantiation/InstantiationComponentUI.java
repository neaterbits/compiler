package dev.nimbler.ide.component.common.instantiation;

import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.ui.ComponentDialogContext;
import dev.nimbler.ide.component.common.ui.ComponentUI;

public interface InstantiationComponentUI extends ComponentUI {

    void openNewableDialog(
            ComponentDialogContext uiContext,
            NewableCategoryName category,
            Newable newable,
            SourceFolderResourcePath sourceFolder,
            NamespaceResourcePath namespace,
            ComponentIDEAccess ideAccess);
}
