package dev.nimbler.ide.component.common.instantiation;

import java.util.List;

import dev.nimbler.ide.component.common.IDEComponent;

public interface InstantiationComponent extends IDEComponent {

	List<NewableCategory> getNewables();

}
