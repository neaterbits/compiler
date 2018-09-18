package com.neaterbits.compiler.common.ast.expression;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class ParameterList extends BaseASTElement {

	private final ASTList<Expression> list;

	public ParameterList(Context context, List<Expression> list) {
		super(context);

		this.list = makeList(list);
	}

	public ASTList<Expression> getList() {
		return list;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(list, recurseMode, visitor);
	}
}
