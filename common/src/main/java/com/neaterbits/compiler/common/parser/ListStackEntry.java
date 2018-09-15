package com.neaterbits.compiler.common.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.BaseASTElement;

public abstract class ListStackEntry<T extends BaseASTElement> extends StackEntry {
	private final List<T> list;
	
	protected ListStackEntry() {
		this.list = new ArrayList<>();
	}
	
	public final void add(T entry) {
		Objects.requireNonNull(entry);
		
		this.list.add(entry);
	}
	
	public final T getLast() {
		return list.get(list.size() - 1);
	}
	
	public final List<T> getList() {
		return Collections.unmodifiableList(list);
	}
}
