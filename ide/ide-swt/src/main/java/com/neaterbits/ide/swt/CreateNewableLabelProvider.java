package com.neaterbits.ide.swt;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import com.neaterbits.ide.component.common.instantiation.Named;

final class CreateNewableLabelProvider extends LabelProvider implements ILabelProvider {

	@Override
	public String getText(Object element) {
		return ((Named)element).getDisplayName();
	}
}
