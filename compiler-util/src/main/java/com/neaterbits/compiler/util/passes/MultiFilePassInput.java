package com.neaterbits.compiler.util.passes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

final class MultiFilePassInput extends MultiFileInputOutput<FilePassInput> implements Iterable<FilePassInput> {

	private final Collection<FilePassInput> inputs;
	
	MultiFilePassInput(Collection<? extends FilePassInput> inputs) {
	
		Objects.requireNonNull(inputs);
		
		this.inputs = Collections.unmodifiableCollection(new ArrayList<>(inputs));
	}

	@Override
	public Iterator<FilePassInput> iterator() {
		return inputs.iterator();
	}
	
	final int getCount() {
		return inputs.size();
	}
}
