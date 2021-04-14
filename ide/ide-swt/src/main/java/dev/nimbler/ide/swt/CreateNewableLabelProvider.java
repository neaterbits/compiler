package dev.nimbler.ide.swt;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import dev.nimbler.ide.component.common.instantiation.Named;

final class CreateNewableLabelProvider extends LabelProvider implements ILabelProvider {

	@Override
	public String getText(Object element) {
		return ((Named)element).getDisplayName();
	}
}
