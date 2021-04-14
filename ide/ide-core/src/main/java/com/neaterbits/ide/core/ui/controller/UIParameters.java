package com.neaterbits.ide.core.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;

public final class UIParameters {

	private final Translator translator;
	private final KeyBindings keyBindings;
	private final UIModels uiModels;
	private final IDEComponentsConstAccess componentsAccess;
	private final TextEditorConfig textEditorConfig;

	public UIParameters(
	        Translator translator,
	        KeyBindings keyBindings,
	        UIModels uiModels,
	        IDEComponentsConstAccess componentsAccess,
	        TextEditorConfig textEditorConfig) {
		
		Objects.requireNonNull(translator);
		Objects.requireNonNull(keyBindings);
		Objects.requireNonNull(uiModels);
		Objects.requireNonNull(componentsAccess);
		Objects.requireNonNull(textEditorConfig);
		
		this.translator = translator;
		this.keyBindings = keyBindings;
		this.uiModels = uiModels;
		this.componentsAccess = componentsAccess;
		this.textEditorConfig = textEditorConfig;
	}

	public Translator getTranslator() {
		return translator;
	}
	
	public KeyBindings getKeyBindings() {
		return keyBindings;
	}

	public UIModels getUIModels() {
		return uiModels;
	}

	public IDEComponentsConstAccess getComponentsAccess() {
        return componentsAccess;
    }

    public TextEditorConfig getTextEditorConfig() {
		return textEditorConfig;
	}
}
