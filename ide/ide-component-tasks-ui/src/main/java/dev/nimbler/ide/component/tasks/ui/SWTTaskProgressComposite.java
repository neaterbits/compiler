package dev.nimbler.ide.component.tasks.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

final class SWTTaskProgressComposite extends Composite {

	private final Label taskNameLabel;
	private final ProgressBar taskProgress;
	
	SWTTaskProgressComposite(Composite composite, int style, boolean progressKnown) {
		super(composite, style);

		setLayout(new FillLayout(SWT.VERTICAL));
		
		this.taskNameLabel = new Label(this, SWT.NONE);

		this.taskProgress = progressKnown ? new ProgressBar(this, SWT.NONE) : null;
	}

	void update(String title) {
		taskNameLabel.setText(title);
	}
		
	void update(int progress, int numItems) {
		taskProgress.setSelection(progress);
		taskProgress.setMaximum(numItems);
	}
}
