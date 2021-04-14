package dev.nimbler.ide.swt;

import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dev.nimbler.ide.common.ui.SearchDirection;
import dev.nimbler.ide.common.ui.SearchScope;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.core.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.core.ui.translation.texts.FindReplaceTexts;
import dev.nimbler.ide.core.ui.view.ButtonListener;
import dev.nimbler.ide.core.ui.view.TextInputChangeListener;
import dev.nimbler.ide.core.ui.view.TextInputEnterKeyListener;
import dev.nimbler.ide.core.ui.view.dialogs.FindReplaceDialog;

final class SWTFindReplaceDialog extends Dialog implements FindReplaceDialog {

	private static final int TEXT_WIDTH = 250;
	
	private final FindReplaceDialogModel initialModel;
	private final Translator translator;
	private final Consumer<FindReplaceDialog> onCreated;

	private Text findText;
	private Text replaceText;
	
	private Button forwardButton;
	private Button backwardButton;

	private Button allButton;
	private Button selectedLinesButton;
	
	private Button caseSensitiveButton;
	private Button wrapSearchButton;
	private Button wholeWordButton;
	
	private Button findButton;
	private Button replaceFindButton;
	private Button replaceButton;
	private Button replaceAllButton;
	
	private Button closeButton;
	
	public SWTFindReplaceDialog(Shell parentShell, FindReplaceDialogModel initialModel, Translator translator, Consumer<FindReplaceDialog> onCreated) {
		super(parentShell);
		
		Objects.requireNonNull(translator);
		Objects.requireNonNull(initialModel);
		
		this.initialModel = initialModel;
		this.translator = translator;
		this.onCreated = onCreated;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite)super.createDialogArea(parent);

		final GridLayout outerLayout = SWTLayoutUtil.makeGridLayout(
				1,
				UIDimensions.DIALOG_OUTER_MARGIN_WIDTH,
				UIDimensions.DIALOG_OUTER_MARGIN_HEIGHT,
				UIDimensions.DIALOG_OUTER_SPACING);
				
		composite.setLayout(outerLayout);
		
		final Composite findReplaceTextComposite = new Composite(composite, SWT.NONE);
		
		findReplaceTextComposite.setLayout(new GridLayout(2, false));
		findReplaceTextComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		
		final Label findLabel = new Label(findReplaceTextComposite, SWT.NONE);
		findLabel.setText(translator.translate(FindReplaceTexts.FIND_TEXT_LABEL));
		findLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		this.findText = new Text(findReplaceTextComposite, SWT.BORDER);
		
		final GridData findTextGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		findTextGridData.widthHint = TEXT_WIDTH;
		findTextGridData.heightHint = UIDimensions.TEXT_HEIGHT;
		findText.setLayoutData(findTextGridData);
		
		if (initialModel.getSearchFor() != null && !initialModel.getSearchFor().isEmpty()) {
			findText.setText(initialModel.getSearchFor());
		}

		final Label replaceLabel = new Label(findReplaceTextComposite, SWT.NONE);
		replaceLabel.setText(translator.translate(FindReplaceTexts.REPLACE_WITH_TEXT_LABEL));
		replaceLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		this.replaceText = new Text(findReplaceTextComposite, SWT.BORDER);
		
		final GridData replaceTextGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		replaceTextGridData.widthHint = TEXT_WIDTH;
		replaceTextGridData.heightHint = UIDimensions.TEXT_HEIGHT;
		replaceText.setLayoutData(replaceTextGridData);
		if (initialModel.getReplaceWith() != null && !initialModel.getReplaceWith().isEmpty()) {
			replaceText.setText(initialModel.getReplaceWith());
		}

		final Composite directionAndScopeComposite = new Composite(composite, SWT.NONE);

		directionAndScopeComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		
		directionAndScopeComposite.setLayout(SWTLayoutUtil.makeFillLayout(SWT.HORIZONTAL, 0, 0, UIDimensions.DIALOG_INNER_SPACING));
		
		final Group directionGroup = new Group(directionAndScopeComposite, SWT.NONE);
		directionGroup.setLayout(new GridLayout(1, false));
		directionGroup.setText(translator.translate(FindReplaceTexts.DIRECTION));
		
		
		this.forwardButton = new Button(directionGroup, SWT.RADIO);
		forwardButton.setText(translator.translate(FindReplaceTexts.FORWARD));
		forwardButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		this.backwardButton = new Button(directionGroup, SWT.RADIO);
		backwardButton.setText(translator.translate(FindReplaceTexts.BACKWARD));
		backwardButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		if (initialModel.getDirection() == SearchDirection.FORWARD) {
			forwardButton.setSelection(true);
		}
		else if (initialModel.getDirection() == SearchDirection.BACKWARD) {
			backwardButton.setSelection(true);
		}
		
		final Group scopeGroup = new Group(directionAndScopeComposite, SWT.NONE);
		scopeGroup.setLayout(new GridLayout(1, false));
		scopeGroup.setText(translator.translate(FindReplaceTexts.SCOPE));
		
		this.allButton = new Button(scopeGroup, SWT.RADIO);
		allButton.setText(translator.translate(FindReplaceTexts.ALL));
		allButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		this.selectedLinesButton = new Button(scopeGroup, SWT.RADIO);
		selectedLinesButton.setText(translator.translate(FindReplaceTexts.SELECTED_LINES));
		selectedLinesButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		if (initialModel.getScope() == SearchScope.ALL) {
			allButton.setSelection(true);
		}
		else if (initialModel.getScope() == SearchScope.SELECTED_LINES) {
			selectedLinesButton.setSelection(true);
		}
		
		final Group optionsGroup = new Group(composite, SWT.NONE);
		
		optionsGroup.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));

		optionsGroup.setLayout(new GridLayout(2, false));
		optionsGroup.setText(translator.translate(FindReplaceTexts.OPTIONS));
		
		this.caseSensitiveButton = new Button(optionsGroup, SWT.CHECK);
		caseSensitiveButton.setText(translator.translate(FindReplaceTexts.CASE_SENSITIVE));
		caseSensitiveButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		caseSensitiveButton.setSelection(initialModel.isCaseSensitive());
		
		this.wrapSearchButton = new Button(optionsGroup, SWT.CHECK);
		wrapSearchButton.setText(translator.translate(FindReplaceTexts.WRAP_SEARCH));
		wrapSearchButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		wrapSearchButton.setSelection(initialModel.isWrap());

		this.wholeWordButton = new Button(optionsGroup, SWT.CHECK);
		wholeWordButton.setText(translator.translate(FindReplaceTexts.WHOLE_WORD));
		wholeWordButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		wholeWordButton.setSelection(initialModel.isWholeWord());
		
		final Composite actionButtonsComposite = new Composite(composite, SWT.NONE);
		
		actionButtonsComposite.setLayoutData(new GridData(SWT.END, SWT.BEGINNING, false, false));
		
		actionButtonsComposite.setLayout(new GridLayout(2, true));
		
		this.findButton = new Button(actionButtonsComposite, SWT.PUSH);
		findButton.setText(translator.translate(FindReplaceTexts.FIND_BUTTON));
		findButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		
		this.replaceFindButton = new Button(actionButtonsComposite, SWT.PUSH);
		replaceFindButton.setText(translator.translate(FindReplaceTexts.REPLACE_FIND_BUTTON));
		replaceFindButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));

		this.replaceButton = new Button(actionButtonsComposite, SWT.PUSH);
		replaceButton.setText(translator.translate(FindReplaceTexts.REPLACE_BUTTON));
		replaceButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));

		this.replaceAllButton = new Button(actionButtonsComposite, SWT.PUSH);
		replaceAllButton.setText(translator.translate(FindReplaceTexts.REPLACE_ALL_BUTTON));
		replaceAllButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));

		final Label label = new Label(actionButtonsComposite, SWT.NONE);
		label.setLayoutData(new GridData());
		
		this.closeButton = new Button(actionButtonsComposite, SWT.PUSH);
		closeButton.setText(translator.translate(FindReplaceTexts.CLOSE_BUTTON));
		
		final GridData closeButtonGridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		closeButtonGridData.verticalIndent = 10;
		closeButton.setLayoutData(closeButtonGridData);
		
		addButtonListener(closeButton, this::close);
		
		if (onCreated != null) {
			onCreated.accept(this);
		}
		
		return composite;
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(translator.translate(FindReplaceTexts.FIND_REPLACE_TITLE));
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}

	@Override
	public void setFindTextSelected() {
		findText.setSelection(0, findText.getCharCount());
	}

	@Override
	public void addFindTextChangeListener(TextInputChangeListener listener) {
		findText.addModifyListener(e -> listener.onTextChange(findText.getText()));
	}
	
	@Override
	public void addFindTextEnterKeyListener(TextInputEnterKeyListener listener) {
		findText.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				listener.onEnterKey();
			}
		});
	}

	@Override
	public void addReplaceWithTextListener(TextInputChangeListener listener) {
		replaceText.addModifyListener(e -> listener.onTextChange(replaceText.getText()));
	}

	private static void addButtonListener(Button button, ButtonListener listener) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listener.onSelected();
			}
		});
	}
	
	@Override
	public void addDirectionForwardListener(ButtonListener listener) {
		addButtonListener(forwardButton, listener);
	}

	@Override
	public void addDirectionBackwardListener(ButtonListener listener) {
		addButtonListener(backwardButton, listener);
	}

	@Override
	public void addScopeAllListener(ButtonListener listener) {
		addButtonListener(allButton, listener);
	}

	@Override
	public void addScopeSelectedLinesListener(ButtonListener listener) {
		addButtonListener(selectedLinesButton, listener);
	}

	@Override
	public void addOptionsCaseSensitiveListener(ButtonListener listener) {
		addButtonListener(caseSensitiveButton, listener);
	}

	@Override
	public void addOptionsWrapSearchListener(ButtonListener listener) {
		addButtonListener(wrapSearchButton, listener);
	}

	@Override
	public void addOptionsWholeWordListener(ButtonListener listener) {
		addButtonListener(wholeWordButton, listener);
	}

	@Override
	public void addFindButtonListener(ButtonListener listener) {
		addButtonListener(findButton, listener);
	}

	@Override
	public void addReplaceFindButtonListener(ButtonListener listener) {
		addButtonListener(replaceFindButton, listener);
	}

	@Override
	public void addReplaceButtonListener(ButtonListener listener) {
		addButtonListener(replaceButton, listener);
	}

	@Override
	public void addReplaceAllButtonListener(ButtonListener listener) {
		addButtonListener(replaceAllButton, listener);
	}

	@Override
	public void setFindButtonEnabled(boolean enabled) {
		findButton.setEnabled(enabled);
	}

	@Override
	public void setReplaceFindButtonEnabled(boolean enabled) {
		replaceFindButton.setEnabled(enabled);
	}

	@Override
	public void setReplaceButtonEnabled(boolean enabled) {
		replaceButton.setEnabled(enabled);
	}

	@Override
	public void setReplaceAllButtonEnabled(boolean enabled) {
		replaceAllButton.setEnabled(enabled);
	}

	@Override
	public FindReplaceDialogModel getModel() {
		
		return new FindReplaceDialogModel(
				findText.getText(),
				replaceText.getText(),
				forwardButton.getSelection() ? SearchDirection.FORWARD : SearchDirection.BACKWARD,
				allButton.getSelection() ? SearchScope.ALL : SearchScope.SELECTED_LINES,
				caseSensitiveButton.getSelection(),
				wrapSearchButton.getSelection(),
				wholeWordButton.getSelection());
	}
}
