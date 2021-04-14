package com.neaterbits.ide.component.java.ui;

import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.ui.ComponentDialogContext;
import com.neaterbits.ide.component.common.instantiation.InstantiationComponentUI;
import com.neaterbits.ide.component.common.instantiation.Newable;
import com.neaterbits.ide.component.common.instantiation.NewableCategoryName;
import com.neaterbits.ide.ui.swt.SWTDialogUIContext;

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
