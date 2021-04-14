package com.neaterbits.ide.component.compiledfiledebug.ui;

import java.util.Collection;
import java.util.Objects;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.controller.EditorsListener;
import com.neaterbits.ide.common.ui.view.ActionContextListener;

public final class SWTCompiledFileView
        implements CompiledFileView, EditorsListener {

	private static final boolean VIEW_PLACEHOLDER_ELEMENTS = true;
	
	private final Composite compiledFileComposite;
	
	private final StyledText textWidget;
	private final Label cursorOffsetLabel;
	private final Label tokenTypeLabel;
	private final Label tokenTypeNameLabel;
	private final Color highlightColor;
	private final ListViewer errorsViewer;
	
	private long editorCursorOffset;
	private ISourceToken curToken;
	private SourceFileModel sourceFileModel;
	
	public SWTCompiledFileView(Composite composite) {

	    this.compiledFileComposite = new Composite(composite, SWT.NONE);

		final GridLayout compositeLayout = new GridLayout(2, false);
		
		compositeLayout.marginWidth = 0;
		
		compiledFileComposite.setLayout(compositeLayout);
		
		this.textWidget = new StyledText(compiledFileComposite, SWT.BORDER);

		final GridData textWidgetData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		textWidget.setLayoutData(textWidgetData);
		
		final Composite infoComposite = new Composite(compiledFileComposite, SWT.NONE);
		
		final GridData infoCompositeData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		
		infoCompositeData.widthHint = 350;
		
		infoComposite.setLayoutData(infoCompositeData);
		
		infoComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		this.cursorOffsetLabel = new Label(infoComposite, SWT.NONE);
		this.tokenTypeLabel = new Label(infoComposite, SWT.NONE);
		this.tokenTypeNameLabel = new Label(infoComposite, SWT.NONE);
		
		updateCursorPosLabels(false);
		
		final Font font = new Font(composite.getDisplay(), new FontData("Monospace", 10, SWT.NONE));
		textWidget.addDisposeListener(event -> font.dispose());
		textWidget.setFont(font);
		
		textWidget.setEditable(false);
		
		this.highlightColor = new Color(null, 0x30, 0xA0, 0x30);
		
		compiledFileComposite.addDisposeListener(event -> highlightColor.dispose());
		
		this.errorsViewer = new ListViewer(compiledFileComposite);
		
		final GridData errorsData = new GridData(SWT.FILL, SWT.FILL, true, false);
		
		errorsData.horizontalSpan = 2;
		
		errorsViewer.getList().setLayoutData(errorsData);
		
		errorsViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {

				final String text;

				if (element instanceof CompileError) {
					final CompileError compileError = (CompileError)element;
				
					text = compileError.getMessage();
				}
				else {
					throw new UnsupportedOperationException();
				}

				return text;
			}
		});
		
		errorsViewer.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {

				final Object [] elements;
				
				if (inputElement instanceof SourceFileModel) {
					
					final SourceFileModel sourceFileModel = (SourceFileModel)inputElement;

					elements = sourceFileModel.getParserErrors().toArray(new Object[sourceFileModel.getParserErrors().size()]);
				}
				else {
					elements = null;
				}
				
				return elements;
			}
		});
	}
	
	Composite getControl() {
	    return compiledFileComposite;
	}
	
	@Override
	public void onEditorCursorPosUpdate(long cursorOffset) {

		this.editorCursorOffset = cursorOffset;
		
		updateViewText(true);
		
		updateCursorPosLabels(true);
	}

	@Override
	public void onSourceFileModelChanged(SourceFileModel model) {
	
		Objects.requireNonNull(model);
		
		this.sourceFileModel = model;
		
		updateViewText(false);
		
		updateCursorPosLabels(true);
		
		errorsViewer.setInput(model);
	}

	
	private void updateCursorPosLabels(boolean showValue) {
		
		cursorOffsetLabel.setText("Cursor offset:" + (showValue ? (" " + editorCursorOffset) : ""));
		tokenTypeLabel.setText("Token:" + (curToken != null ? (" " + curToken.getTokenType().name()) : ""));
		tokenTypeNameLabel.setText("Token type:" + (curToken != null && curToken.getTypeName() != null
																? (" " + curToken.getTypeName().toDebugString())
																: ""));
	}
	
	private void updateViewText(boolean updateCursorOnly) {

		if (sourceFileModel != null) {
			final StringBuilder sb = new StringBuilder();
	
			final SWTCompiledFileViewMakeTextVisitor visitor = new SWTCompiledFileViewMakeTextVisitor(sb, editorCursorOffset);
			
			sourceFileModel.iterate(visitor, VIEW_PLACEHOLDER_ELEMENTS);
			
			if (!updateCursorOnly) {
				textWidget.setText(sb.toString());
			}
	
			final int topIndex = visitor.getStringBuilderLineAtCursorOffset() > 10
						? visitor.getStringBuilderLineAtCursorOffset() - 10
						: 0;
			
			
			// System.out.println("## set textwidget pos to " + topIndex + " of " + sb.length());
			
			textWidget.setTopIndex(topIndex);
	
			textWidget.setStyleRange(new StyleRange(0, textWidget.getText().length(), null, null));
	
			this.curToken = visitor.getFoundCursorToken();
	
			if (visitor.getFoundCursorToken() != null) {
				
				// System.out.println("## found cursor token " + visitor.getFoundCursorToken().getTokenDebugString());
				
				textWidget.setStyleRange(new StyleRange(
						(int)visitor.getFoundStringBuilderTextOffset(),
						(int)visitor.getFoundLength(),
						highlightColor,
						null));
			}
		}
	}


	@Override
	public Collection<ActionContext> getActiveActionContexts() {
		return null;
	}

	@Override
	public void addActionContextListener(ActionContextListener listener) {
		
	}
}
