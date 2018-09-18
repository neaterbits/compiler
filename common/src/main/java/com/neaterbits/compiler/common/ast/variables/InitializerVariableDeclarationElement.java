package com.neaterbits.compiler.common.ast.variables;


import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
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

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		super.doRecurse(recurseMode, visitor);
		
		if (initializer != null) {
			doIterate(initializer, recurseMode, visitor);
		}
	}
}
