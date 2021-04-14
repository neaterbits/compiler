package com.neaterbits.ide.component.java.ui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.instantiation.Newable;
import com.neaterbits.ide.component.java.language.JavaLanguageComponent;
import com.neaterbits.util.Strings;

public class NewJavaTypeDialog extends Dialog {

	private final String title;
	private final Newable newable;
	private final SourceFolderResourcePath sourceFolder;
	private final NamespaceResourcePath namespace;
	private final ComponentIDEAccess ideAccess;

	private Text sourceFolderText;
	private Text packageText;
	private Text nameText;
	
	public NewJavaTypeDialog(
			Shell parentShell,
			String title,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess) {
		super(parentShell);
		
		Objects.requireNonNull(title);
		Objects.requireNonNull(newable);
		Objects.requireNonNull(ideAccess);
		
		this.title = title;
		this.newable = newable;
		this.sourceFolder = sourceFolder;
		this.namespace = namespace;
		this.ideAccess = ideAccess;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		final Composite composite = (Composite)super.createDialogArea(parent);

		composite.setLayout(new GridLayout(3, false));
		
		final Label sourceFolderLabel = new Label(composite, SWT.NONE);
		sourceFolderLabel.setText("Source folder:");
		
		this.sourceFolderText = new Text(composite, SWT.BORDER);
		sourceFolderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		if (sourceFolder != null) {
			sourceFolderText.setText(
					  sourceFolder.getModule().getName()
				+ '/' + sourceFolder.getName()
			);
		}
		
		sourceFolderText.addModifyListener(event -> updateOKButton());

		final Button sourceFolderBrowseButton = new Button(composite, SWT.NONE);
		sourceFolderBrowseButton.setText("Browse");
		
		final Label packageLabel = new Label(composite, SWT.NONE);
		packageLabel.setText("Package:");
		
		this.packageText = new Text(composite, SWT.BORDER);
		packageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		if (namespace != null) {
			packageText.setText(Strings.join(namespace.getNamespaceResource().getNamespace(), '.'));
		}

		packageText.addModifyListener(event -> updateOKButton());
		
		final Button packageBrowseButton = new Button(composite, SWT.NONE);
		packageBrowseButton.setText("Browse");

		final Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("Name:");
		
		this.nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		nameText.addModifyListener(event -> updateOKButton());
		
		nameText.setFocus();
		
		return composite;
	}
	
	
	private SourceFolder getSourceFolder() {
		final String string = sourceFolderText.getText().trim();
	
		final String [] parts = Strings.split(string, '/');
	
		final SourceFolder sourceFolder;
		
		if (parts.length < 2) {
			sourceFolder = null;
		}
		else {
			
			final String moduleName = parts[0];
			
			final String sourcePathName = Strings.join(
					Arrays.asList(parts),
					'/',
					1,
					parts.length - 1,
					part -> part);
			
			sourceFolder = new SourceFolder(moduleName, sourcePathName);
		}
		
		return sourceFolder;
	}
	
	private static class SourceFolder {
		private final String moduleName;
		private final String sourceFolder;
		
		SourceFolder(String projectName, String sourceFolder) {
			this.moduleName = projectName;
			this.sourceFolder = sourceFolder;
		}
	}
	
	private void updateOKButton() {
		
		final Button okButton = this.getButton(IDialogConstants.OK_ID);
		
		if (okButton == null) {
			throw new IllegalStateException();
		}
		
		
		okButton.setEnabled(isValidSourceFolder() && isValidPackageName() && isValidTypeName());
	}
	
	private boolean isValidSourceFolder() {
		final SourceFolder sourceFolder = getSourceFolder();

		return sourceFolder != null
				? ideAccess.isValidSourceFolder(sourceFolder.moduleName, sourceFolder.sourceFolder)
				: false;
	}
	
	private boolean isValidPackageName() {
		
		final String packageName = packageText.getText().trim();

		final String [] parts = Strings.split(packageName, '.');
		
		return parts.length > 0 && Arrays.stream(parts).allMatch(NewJavaTypeDialog::isValidJavaName);
	}
	
	private boolean isValidTypeName() {
		return isValidJavaName(nameText.getText().trim());
	}
	
	private static boolean isValidJavaName(String string) {
		
		boolean isValidJavaName;
		
		if (string.isEmpty()) {
			isValidJavaName = false;
		}
		else {
			if (!Character.isJavaIdentifierStart(string.charAt(0))) {
				isValidJavaName = false;
			}
			else {
				isValidJavaName = true;
				
				for (int i = 1; i < string.length(); ++ i) {
					if (!Character.isJavaIdentifierPart(string.charAt(i))) {
						isValidJavaName = false;
						break;
					}
				}
			}
		}
		
		return isValidJavaName;
	}
	
	private void writeSourceFile() {
		
		final String type;
		
		if (newable.equals(JavaLanguageComponent.CLASS)) {
			type = "class";
		}
		else if (newable.equals(JavaLanguageComponent.INTERFACE)) {
			type = "interface";
		}
		else if (newable.equals(JavaLanguageComponent.ENUM)) {
			type = "enum";
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(packageText.getText().trim()).append(";\n");
		
		sb.append("\n");
		
		sb.append("final ").append(type).append(' ').append(nameText.getText().trim()).append(" {\n");
		
		sb.append("\n");
		
		sb.append("}\n");
		
		final SourceFolder sourceFolder = getSourceFolder();
		
		final String fileName = nameText.getText().trim() + ".java";
		
		try {
			ideAccess.writeAndOpenFile(
					sourceFolder.moduleName,
					sourceFolder.sourceFolder,
					Strings.split(packageText.getText().trim(), '.'),
					fileName,
					sb.toString());
		} catch (IOException ex) {
			MessageDialog.openError(getShell(), "Failed to write file", ex.getMessage());
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 350);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		writeSourceFile();

		super.okPressed();
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		
		final Control control = super.createButtonBar(parent);
		
		updateOKButton();
		
		return control;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(title);
	}
}
