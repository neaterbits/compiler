package dev.nimbler.ide.component.java.ui;

import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.instantiation.InstantiationComponentUI;
import dev.nimbler.ide.component.common.instantiation.Newable;
import dev.nimbler.ide.component.common.instantiation.NewableCategoryName;
import dev.nimbler.ide.component.common.ui.ComponentDialogContext;
import dev.nimbler.ide.ui.swt.SWTDialogUIContext;

public final class JavaUIComponentProvider implements InstantiationComponentUI {

	@Override
	public void openNewableDialog(
			ComponentDialogContext uiContext,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess) {

		final SWTDialogUIContext swtContext = (SWTDialogUIContext)uiContext;
		
		final NewJavaTypeDialog dialog = new NewJavaTypeDialog(
				swtContext.getWindow(),
				"New " + newable.getDisplayName(),
				newable,
				sourceFolder,
				namespace,
				ideAccess);

		dialog.open();
	}
}
