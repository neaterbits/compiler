package com.neaterbits.ide.component.common.instantiation;

public class NewableCategoryName extends Named {

	public NewableCategoryName(String name, String displayName) {
		super(name, displayName);
	}

	public NewableCategoryName(NewableCategoryName name) {
		super(name);
	}
}
