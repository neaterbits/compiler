package com.neaterbits.ide.core.ui.view;

import java.util.Collection;
import java.util.function.Consumer;

import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.model.codemap.TypeSuggestion;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.instantiation.InstantiationComponentUI;
import com.neaterbits.ide.component.common.instantiation.Newable;
import com.neaterbits.ide.component.common.instantiation.NewableCategory;
import com.neaterbits.ide.component.common.instantiation.NewableCategoryName;
import com.neaterbits.ide.core.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.core.ui.model.dialogs.NewableSelection;
import com.neaterbits.ide.core.ui.model.dialogs.OpenTypeDialogModel;
import com.neaterbits.ide.core.ui.view.dialogs.FindReplaceDialog;

public interface UIDialogs {

	TypeSuggestion askOpenType(OpenTypeDialogModel model);

	NewableSelection askCreateNewable(Collection<NewableCategory> categories);

	void openNewableDialog(
			InstantiationComponentUI uiComponent,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess);

	void askFindReplace(FindReplaceDialogModel lastModel, Consumer<FindReplaceDialog> onCreated);
	
}
