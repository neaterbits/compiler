package dev.nimbler.ide.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
    private final List<SourceFileResourcePath> editedFiles;
	
	private SourceFileResourcePath currentEditedFile;
	
	
	public SWTEditorsView(SWTViewList viewList, Composite composite, TextEditorConfig config) {
		
		this.viewList = viewList;
		this.config = config;
		this.tabFolder = new TabFolder(composite, SWT.NONE);
		
		this.editedFiles = new ArrayList<>();
		
		tabFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                
                final int selectionIndex = tabFolder.getSelectionIndex();
                
                if (selectionIndex < 0) {
                    SWTEditorsView.this.currentEditedFile = null;
                }
                else {
                    SWTEditorsView.this.currentEditedFile = editedFiles.get(selectionIndex);
                }
            }
        });
		
		viewList.addView(this, tabFolder);

		// this.composite = new Composite(composite, SWT.NONE);
		
		// this.composite.setLayout(new FillLayout());

		this.editorViews = new HashMap<>();
	}
	
	public TabFolder getTabFolder() {
		return tabFolder;
	}

	@Override
	public EditorView displayFile(
	        final SourceFileResourcePath sourceFile,
	        TextStylingModel textStylingModel,
	        EditorSourceActionContextProvider editorSourceActionContextProvider) {

		Objects.requireNonNull(sourceFile);
		
		this.currentEditedFile = sourceFile;
		editedFiles.add(sourceFile);
		
		SWTEditorView editorView = editorViews.get(sourceFile);
		
		if (editorView == null) {

			editorView = new SWTStyledTextEditorView(
					viewList,
					this.tabFolder,
					config,
					textStylingModel,
					sourceFile,
					editorSourceActionContextProvider,
					() -> onClosedFile(sourceFile));
			
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
	    
	    return currentEditedFile;
	}
	
	private void onClosedFile(SourceFileResourcePath sourceFile) {
	    
	    if (sourceFile == currentEditedFile) {
	        this.currentEditedFile = null;
	    }
	    
	    if (!editedFiles.contains(sourceFile)) {
	        throw new IllegalStateException();
	    }

	    editedFiles.remove(sourceFile);
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
