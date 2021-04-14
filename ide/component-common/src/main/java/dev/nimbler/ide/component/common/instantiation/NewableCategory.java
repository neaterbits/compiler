package dev.nimbler.ide.component.common.instantiation;

import java.util.List;
import java.util.Objects;

public final class NewableCategory extends NewableCategoryName {

	private final List<Newable> types;

	public NewableCategory(String name, String displayName, List<Newable> types) {
		super(name, displayName);
		
		Objects.requireNonNull(types);
		
		this.types = types;
	}

	public NewableCategory(NewableCategoryName name, List<Newable> types) {
		super(name);
		
		this.types = types;
	}

	public List<Newable> getTypes() {
		return types;
	}
}
