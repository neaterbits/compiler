package com.neaterbits.ide.component.common;

import java.util.List;

import com.neaterbits.ide.component.common.instantiation.InstantiationComponentUI;
import com.neaterbits.ide.component.common.instantiation.Newable;
import com.neaterbits.ide.component.common.instantiation.NewableCategory;
import com.neaterbits.ide.component.common.instantiation.NewableCategoryName;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.component.common.ui.DetailsComponentUI;

public interface IDEComponentsConstAccess {

	Languages getLanguages();
	
	List<NewableCategory> getNewableCategories();
	
	InstantiationComponentUI findInstantiationUIComponent(NewableCategoryName category, Newable newable);
	
	List<DetailsComponentUI<?>> getDetailsComponentUIs();
}
