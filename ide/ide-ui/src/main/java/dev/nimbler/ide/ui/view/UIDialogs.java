package dev.nimbler.ide.ui.view;

import java.util.Collection;
import java.util.function.Consumer;

import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.instantiation.InstantiationComponentUI;
import dev.nimbler.ide.component.common.instantiation.Newable;
import dev.nimbler.ide.component.common.instantiation.NewableCategory;
import dev.nimbler.ide.component.common.instantiation.NewableCategoryName;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.model.dialogs.NewableSelection;
import dev.nimbler.ide.ui.model.dialogs.OpenTypeDialogModel;
import dev.nimbler.ide.ui.view.dialogs.FindReplaceDialog;

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
