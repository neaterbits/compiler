package com.neaterbits.ide.component.common.instantiation;

import java.util.List;

import com.neaterbits.ide.component.common.IDEComponent;

public interface InstantiationComponent extends IDEComponent {

	List<NewableCategory> getNewables();

}
