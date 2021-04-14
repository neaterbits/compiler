package dev.nimbler.ide.swt;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.neaterbits.ide.util.swt.TreeContentAdapter;

import dev.nimbler.ide.component.common.instantiation.Newable;
import dev.nimbler.ide.component.common.instantiation.NewableCategory;

final class CreateNewableContentProvider extends TreeContentAdapter implements ITreeContentProvider {

	private Collection<NewableCategory> categories;
	
	public CreateNewableContentProvider(Collection<NewableCategory> categories) {
		this.categories = categories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.categories = (Collection<NewableCategory>)newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		
		final Object[] result;
		
		if (inputElement instanceof Collection<?>) {
			final Collection<?> inputCollection = (Collection<?>)inputElement;
		
			result = inputCollection.toArray(new Object[inputCollection.size()]);
		}
		else if (inputElement instanceof NewableCategory) {
			final NewableCategory newableCategory = (NewableCategory)inputElement;
		
			result = newableCategory.getTypes().toArray(new Newable[newableCategory.getTypes().size()]);
		}
		else if (inputElement instanceof Newable) {
			result = null;
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return result;
	}

	@Override
	public Object getParent(Object element) {
		
		return categories.stream()
				.filter(category -> category.getTypes().contains(element))
				.findFirst()
				.orElse(null);
	}
}
