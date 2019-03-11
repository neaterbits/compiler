package com.neaterbits.compiler.ast.expression;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BasePlaceholderASTElement;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.util.TypeName;

public final class ParameterList extends BasePlaceholderASTElement {

	private final ASTList<Expression> list;

	public ParameterList(List<Expression> list) {
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
	
	public final TypeName [] getTypeNames() {
		
		final NamedType [] types = getTypes();
		
		final TypeName [] typeNames = new TypeName[types.length];
		
		for (int i = 0; i < types.length; ++ i) {
			typeNames[i] = types[i].getCompleteName().toTypeName();
		}
		
		return typeNames;
	}
	
	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(list, recurseMode, iterator);
	}
}
