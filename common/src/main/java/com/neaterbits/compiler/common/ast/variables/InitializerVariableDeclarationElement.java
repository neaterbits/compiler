package com.neaterbits.compiler.common.ast.variables;


import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public class InitializerVariableDeclarationElement extends VariableDeclarationElement {

	private final ASTSingle<Expression> initializer;
	
	public InitializerVariableDeclarationElement(Context context, TypeReference type, VarName name, int numDims, Expression initializer) {
		super(context, type, name, numDims);

		this.initializer = initializer != null ? makeSingle(initializer) : null;
	}
	
	public final Expression getInitializer() {
		return initializer.get();
	}
}
