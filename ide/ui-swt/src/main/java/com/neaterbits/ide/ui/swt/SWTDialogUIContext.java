package com.neaterbits.ide.ui.swt;

import java.util.Objects;

import org.eclipse.swt.widgets.Shell;

import com.neaterbits.ide.component.common.ui.ComponentDialogContext;

public final class SWTDialogUIContext extends ComponentDialogContext {

	private final Shell window;

	public SWTDialogUIContext(Shell window) {

		Objects.requireNonNull(window);
		
		this.window = window;
	}

	public Shell getWindow() {
		return window;
	}
}
