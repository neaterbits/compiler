package dev.nimbler.ide.swt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.common.ui.view.ActionContextListener;
import dev.nimbler.ide.ui.swt.SWTView;
import dev.nimbler.ide.ui.swt.SWTViewList;
import dev.nimbler.ide.ui.view.EditorSourceActionContextProvider;
import dev.nimbler.ide.ui.view.EditorView;
import dev.nimbler.ide.ui.view.EditorsView;
import dev.nimbler.ide.util.ui.text.styling.TextStylingModel;

public final class SWTEditorsView extends SWTView implements EditorsView {

	private final SWTViewList viewList;
	private final TextEditorConfig config;
	private final TabFolder tabFolder;
	
	// private final Composite composite;
	
	private final Map<SourceFileResourcePath, SWTEditorView> editorViews;
	
	public SWTEditorsView(SWTViewList viewList, Composite composite, TextEditorConfig config) {
		
		this.viewList = viewList;
		this.config = config;
		this.tabFolder = new TabFolder(composite, SWT.NONE);
		
		viewList.addView(this, tabFolder);

		// this.composite = new Composite(composite, SWT.NONE);
		
		// this.composite.setLayout(new FillLayout());

		this.editorViews = new HashMap<>();
	}
	
	public TabFolder getTabFolder() {
		return tabFolder;
	}

	@Override
	public EditorView displayFile(SourceFileResourcePath sourceFile, TextStylingModel textStylingModel, EditorSourceActionContextProvider editorSourceActionContextProvider) {

		Objects.requireNonNull(sourceFile);
		
		SWTEditorView editorView = editorViews.get(sourceFile);
		
		if (editorView == null) {

			editorView = new SWTStyledTextEditorView(
					viewList,
					this.tabFolder,
					config,
					textStylingModel,
					sourceFile,
					editorSourceActionContextProvider);
			
			editorViews.put(sourceFile, editorView);
		}
		
		editorView.setSelectedAndFocused();
		
		return editorView;
	}
	
	@Override
	public Collection<ActionContext> getActiveActionContexts() {
		return null;
	}

	@Override
	public void addActionContextListener(ActionContextListener listener) {
		
	}

	@Override
	public SourceFileResourcePath getCurrentEditedFile() {
		
		final int selectionIndex = tabFolder.getSelectionIndex();
		
		final SourceFileResourcePath file;
		
		if (selectionIndex < 0) {
			file = null;
		}
		else {
			file = (SourceFileResourcePath)tabFolder.getItem(selectionIndex).getData();
			
			if (file == null) {
				throw new IllegalStateException();
			}
		}

		return file;
	}

	@Override
	public void closeFile(SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(sourceFile);
		
		final SWTEditorView editorView = editorViews.get(sourceFile);

		if (editorView != null) {
			
			try {
				editorView.close();
			}
			finally {
				editorViews.remove(sourceFile);
			}
		}
	}
}
