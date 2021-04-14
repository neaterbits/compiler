package com.neaterbits.ide.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.clipboard.ClipboardDataType;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.ActionContextListener;
import com.neaterbits.ide.core.ui.actions.contexts.ClipboardPasteableContext;
import com.neaterbits.ide.core.ui.actions.contexts.ClipboardSelectionContext;
import com.neaterbits.ide.core.ui.actions.contexts.EditorContext;
import com.neaterbits.ide.core.ui.actions.contexts.EditorSelectionContext;
import com.neaterbits.ide.core.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.core.ui.view.TextSelectionListener;
import com.neaterbits.ide.core.ui.view.ViewDisposeListener;

abstract class SWTBaseTextEditorView extends SWTEditorView {

	private final TabFolder composite;
	private final TabItem tabItem;

	private TextEditorConfig config;
	
	private final SourceFileResourcePath sourceFile;

	private final EditorSourceActionContextProvider editorSourceActionContextProvider;
	
	abstract void setCursorPos(int pos);
	abstract int getCursorPos();
	abstract void setFocus();
	abstract void setTabs(int tabs);
	
	abstract void addKeyListener(KeyListener keyListener);
	abstract void addTextSelectionListener(TextSelectionListener textSelectionListener);
	
	abstract boolean hasSelectedText();
	
	SWTBaseTextEditorView(
			TabFolder composite,
			TextEditorConfig config,
			SourceFileResourcePath sourceFile,
			EditorSourceActionContextProvider editorSourceActionContextProvider) {

		this.composite = composite;

		this.sourceFile = sourceFile;
		
		this.editorSourceActionContextProvider = editorSourceActionContextProvider;
		
		this.tabItem = new TabItem(composite, SWT.NONE);

		tabItem.setText(sourceFile.getName());
		tabItem.setData(sourceFile);
		
		Objects.requireNonNull(config);
		
		this.config = config;
	}
	
	
	@Override
	public void addDisposeListener(ViewDisposeListener listener) {
		tabItem.addDisposeListener(e -> listener.onDispose());
	}
	
	@Override
	public final Collection<ActionContext> getActiveActionContexts() {

		return getActiveActionContexts(hasSelectedText(), getCursorPos());
	}

	private Collection<ActionContext> getActiveActionContexts(boolean hasSelectedText, long cursorPos) {
		
		final List<ActionContext> actionContexts = new ArrayList<>();
		
		final Collection<ActionContext> providerActionContexts = editorSourceActionContextProvider.getActionContexts(cursorPos);
		
		if (providerActionContexts != null) {
			actionContexts.addAll(providerActionContexts);
		}

		actionContexts.add(new EditorContext(sourceFile));
		actionContexts.add(new ClipboardPasteableContext(Arrays.asList(ClipboardDataType.TEXT)));
		
		if (hasSelectedText) {
			actionContexts.add(new EditorSelectionContext());
			actionContexts.add(new ClipboardSelectionContext(ClipboardDataType.TEXT));
		}
		
		return actionContexts;
	}
	
	final void configure(Control tabItemControl) {
		
		Objects.requireNonNull(tabItemControl);
		
		tabItem.setControl(tabItemControl);
		
		composite.setSelection(tabItem);

		configure(config);
		
;
	}
	
	@Override
	public final void addActionContextListener(ActionContextListener listener) {

		addTextSelectionListener(hasSelectedText -> listener.onUpdated(getActiveActionContexts(hasSelectedText, getCursorPos())));
		addCursorPositionListener(cursorPos -> listener.onUpdated(getActiveActionContexts(hasSelectedText(), cursorPos)));
	}
	

	@Override
	public final Collection<ClipboardDataType> getSupportedPasteDataTypes() {
		return Arrays.asList(ClipboardDataType.TEXT);
	}
	
	@Override
	final SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}

	@Override
	final void setSelectedAndFocused() {
		composite.setSelection(tabItem);
		
		setFocus();
	}

	@Override
	final void close() {
		tabItem.dispose();
	}

	@Override
	final void configure(TextEditorConfig config) {
		
		Objects.requireNonNull(config);
		
		setTabs(config.getTabs());
		
		this.config = config;
	}

	@Override
	public final long getCursorPosition() {
		return getCursorPos();
	}

	@Override
	public void setCursorPosition(long offset) {
		setCursorPos((int)offset);
	}
}

