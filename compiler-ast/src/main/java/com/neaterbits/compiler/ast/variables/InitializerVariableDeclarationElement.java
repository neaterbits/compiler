package com.neaterbits.compiler.ast.variables;


import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public class InitializerVariableDeclarationElement extends VariableDeclarationElement {

	private final ASTSingle<Expression> initializer;
	
	public InitializerVariableDeclarationElement(Context context, TypeReference type, VarName name, int numDims, Expression initializer) {
		super(context, type, name, numDims);

		this.initializer = initializer != null ? makeSingle(initializer) : null;
	}
	
	public final Expression getInitializer() {
		return initializer != null ? initializer.get() : null;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		super.doRecurse(recurseMode, iterator);
		
		if (initializer != null) {
			doIterate(initializer, recurseMode, iterator);
		}
	}
}
