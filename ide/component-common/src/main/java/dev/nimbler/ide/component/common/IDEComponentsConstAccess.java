package dev.nimbler.ide.component.common;

import java.util.List;

import dev.nimbler.ide.component.common.instantiation.InstantiationComponentUI;
import dev.nimbler.ide.component.common.instantiation.Newable;
import dev.nimbler.ide.component.common.instantiation.NewableCategory;
import dev.nimbler.ide.component.common.instantiation.NewableCategoryName;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.component.common.ui.DetailsComponentUI;

public interface IDEComponentsConstAccess extends IDEComponentsFinder {

	Languages getLanguages();
	
	List<NewableCategory> getNewableCategories();
	
	InstantiationComponentUI findInstantiationUIComponent(NewableCategoryName category, Newable newable);
	
	List<DetailsComponentUI<?>> getDetailsComponentUIs();
}
