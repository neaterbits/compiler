package com.neaterbits.compiler.common.ast.expression;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.type.NamedType;

public final class ParameterList extends BaseASTElement {

	private final ASTList<Expression> list;

	public ParameterList(Context context, List<Expression> list) {
		super(context);

		this.list = makeList(list);
	}

	public ASTList<Expression> getList() {
		return list;
	}

	private static final NamedType [] NO_TYPES = new NamedType[0];
	
	public final NamedType [] getTypes() {
		
		final NamedType [] types;
		
		if (list.isEmpty()) {
			types = NO_TYPES;
		}
		else {
			types = new NamedType[list.size()];
			
			list.foreachWithIndex((expression, index) -> {
				types[index] = (NamedType)expression.getType();
			});
		}

		return types;
	}
	
	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(list, recurseMode, iterator);
	}
}
