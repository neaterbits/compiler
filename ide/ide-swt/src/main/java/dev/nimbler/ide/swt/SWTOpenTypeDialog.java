package dev.nimbler.ide.swt;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.ide.common.model.codemap.TypeSuggestions;
import dev.nimbler.ide.core.ui.model.dialogs.OpenTypeDialogModel;

final class SWTOpenTypeDialog extends Dialog {

	private final OpenTypeDialogModel model;
	
	private List list;
	
	private java.util.List<TypeSuggestion> suggestions;
	private TypeSuggestion suggestion;
	
	SWTOpenTypeDialog(Shell parentShell, OpenTypeDialogModel model) {
		super(parentShell);

		this.model = model;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		final Composite composite = (Composite)super.createDialogArea(parent);
		
		final GridLayout gridLayout = new GridLayout(1, false);
		
		gridLayout.marginLeft = gridLayout.marginRight = 15;
		
		composite.setLayout(gridLayout);

		final Label label = new Label(composite, SWT.NONE);
		
		label.setText("Enter type name");
		
		label.setLayoutData(new GridData());
		
		final GridData textLayout = new GridData(SWT.FILL, SWT.FILL, true, false);
		
		textLayout.heightHint = UIDimensions.TEXT_HEIGHT;
		
		final Text text = new Text(composite, SWT.BORDER);
		
		text.setLayoutData(textLayout);
		
		final Label suggestionsLabel = new Label(composite, SWT.NONE);
		
		suggestionsLabel.setText("Suggestions");
		
		this.list = new List(composite, SWT.BORDER);
		
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		final ListViewer listViewer = new ListViewer(list);
		
		listViewer.addDoubleClickListener(event -> {
			okPressed();
		});
		
		text.addModifyListener(event -> {
			updateSuggestions(text, list);
		});
		
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.ARROW_DOWN) {
					list.setFocus();
				}
			}
		});
		
		updateSuggestions(text, list);
		
		return composite;
	}
	
	private void updateSuggestions(Text text, List list) {
		final TypeSuggestions suggestions = model.getSuggestions(text.getText().trim());
		
		this.suggestions = new ArrayList<>(suggestions.getTypeSuggestions());
		
		list.removeAll();
		
		for (TypeSuggestion suggestion : this.suggestions) {
			list.add(suggestion.getName());
		}
		
		if (!this.suggestions.isEmpty()) {
			list.setSelection(0);
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}

	@Override
	protected void configureShell(Shell newShell) {
		
		super.configureShell(newShell);
		
		newShell.setText("Open Type");
	}

	@Override
	protected void okPressed() {
		
		final int seletionIndex = list.getSelectionIndex();
		
		this.suggestion = seletionIndex >= 0 ? suggestions.get(seletionIndex) : null;
		
		super.okPressed();
	}

	TypeSuggestion getSuggestion() {
		return suggestion;
	}
}
