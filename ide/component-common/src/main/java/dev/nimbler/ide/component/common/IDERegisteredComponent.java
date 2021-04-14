package dev.nimbler.ide.component.common;

import java.util.Objects;

import dev.nimbler.ide.component.common.ui.ComponentUI;

final class IDERegisteredComponent {

	private final IDEComponent component;
	private final ComponentUI uiComponentProvider;

	IDERegisteredComponent(IDEComponent component, ComponentUI uiComponentProvider) {
		
		this.component = component;
		this.uiComponentProvider = uiComponentProvider;
	}

	IDEComponent getComponent() {
		return component;
	}

	ComponentUI getComponentUI() {
		return uiComponentProvider;
	}
}
