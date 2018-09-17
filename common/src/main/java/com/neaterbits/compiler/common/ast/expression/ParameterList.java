package com.neaterbits.compiler.common.ast.expression;

import java.util.Collections;
import java.util.List;

public final class ParameterList {

	private final List<Expression> list;

	public ParameterList(List<Expression> list) {
		this.list = Collections.unmodifiableList(list);
	}

	public List<Expression> getList() {
		return list;
	}
}
