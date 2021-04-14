package dev.nimbler.compiler.ast.objects.expression;

import java.util.List;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BasePlaceholderASTElement;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.language.common.types.TypeName;

public final class ParameterList extends BasePlaceholderASTElement {

	private final ASTList<Expression> list;

	public ParameterList(List<Expression> list) {
		this.list = makeList(list);
	}

	public ASTList<Expression> getList() {
		return list;
	}

	private static final TypeReference [] NO_TYPES = new TypeReference[0];
	
	public final TypeReference [] getTypes() {
		
		final TypeReference [] types;
		
		if (list.isEmpty()) {
			types = NO_TYPES;
		}
		else {
			types = new TypeReference[list.size()];
			
			list.foreachWithIndex((expression, index) -> {
				types[index] = expression.getType();
			});
		}

		return types;
	}
	
	public final TypeName [] getTypeNames() {
		
		final TypeReference [] types = getTypes();
		
		final TypeName [] typeNames = new TypeName[types.length];
		
		for (int i = 0; i < types.length; ++ i) {
			typeNames[i] = types[i].getTypeName();
		}
		
		return typeNames;
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PARAMETER_LIST;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(list, recurseMode, iterator);
	}
}
