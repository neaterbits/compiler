package com.neaterbits.compiler.common.ast.expression;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class ResourceList extends BaseASTElement {

	private final List<Resource> list;

	public ResourceList(Context context, List<Resource> list) {
		super(context);

		this.list = Collections.unmodifiableList(list);
	}

	public List<Resource> getList() {
		return list;
	}
}
