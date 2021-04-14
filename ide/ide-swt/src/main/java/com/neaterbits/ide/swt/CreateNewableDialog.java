package com.neaterbits.ide.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.ide.component.common.instantiation.Named;
import com.neaterbits.ide.component.common.instantiation.Newable;
import com.neaterbits.ide.component.common.instantiation.NewableCategory;
import com.neaterbits.ide.core.ui.model.dialogs.NewableSelection;

final class CreateNewableDialog extends Dialog {

	private final Collection<NewableCategory> categories;
	
	private NewableSelection selection;
	
	public CreateNewableDialog(Shell parentShell, Collection<NewableCategory> categories) {
		super(parentShell);
		
		Objects.requireNonNull(categories);
		
		this.categories = categories;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite)super.createDialogArea(parent);

		composite.setLayout(new GridLayout(1, false));
		
		final Text text = new Text(composite, SWT.BORDER);
		
		final GridData textGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		
		textGridData.heightHint = UIDimensions.TEXT_HEIGHT;
		
		text.setLayoutData(textGridData);
		
		final TreeViewer treeViewer = new TreeViewer(composite);
		
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		final CreateNewableContentProvider contentProvider = new CreateNewableContentProvider(categories);
		
		treeViewer.setContentProvider(contentProvider);
		
		treeViewer.setInput(categories);
		
		treeViewer.setLabelProvider(new CreateNewableLabelProvider());
		
		treeViewer.setInput(categories);
		
		setExpandedElements(treeViewer);
		
		treeViewer.addSelectionChangedListener(event -> {
			final ITreeSelection selection = (ITreeSelection)event.getSelection();
			
			final Object element = selection.getFirstElement();
			
			if (element instanceof Newable) {
				
				final Newable newable = (Newable)element;
				
				this.selection = new NewableSelection(
						(NewableCategory)contentProvider.getParent(newable),
						newable);
				
			}
		});
		
		text.addModifyListener(event -> {
			final String string = text.getText().trim();
			
			if (!string.isEmpty()) {
				final List<NewableCategory> filtered = filterCategories(categories, string);
				
				treeViewer.setInput(filtered);

				filtered.forEach(category -> treeViewer.setExpandedState(category, true));
				
				filtered.stream()
					.flatMap(category -> category.getTypes().stream())
					.findFirst()
					.ifPresent(newable -> treeViewer.setSelection(new StructuredSelection(newable)));
			}
			else {
				treeViewer.setInput(categories);
				
				setExpandedElements(treeViewer);
			}
		});

		return composite;
	}
	
	private void setExpandedElements(TreeViewer treeViewer) {
		treeViewer.setExpandedElements(new Object [] { categories });
	}
	
	private List<NewableCategory> filterCategories(Collection<NewableCategory> categories, String filter) {
		
		final List<NewableCategory> filtered = new ArrayList<>(categories.size());
		
		for (NewableCategory category : categories) {
			if (filter(category, filter)) {
				filtered.add(category);
			}
			else {
				
				final List<Newable> filteredNewable = new ArrayList<>(category.getTypes().size());
				
				for (Newable newable : category.getTypes()) {
					if (filter(newable, filter)) {
						filteredNewable.add(newable);
					}
				}

				if (!filteredNewable.isEmpty()) {
					filtered.add(new NewableCategory(category, filteredNewable));
				}
			}
		}
		
		return filtered;
	}
	
	private static boolean filter(Named named, String filter) {
		return named.getDisplayName().toLowerCase().startsWith(filter.toLowerCase());
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText("New");
	}

	public NewableSelection getSelection() {
		return selection;
	}
}
