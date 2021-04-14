package com.neaterbits.compiler.ast.objects.variables;


import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public class InitializerVariableDeclarationElement extends VariableDeclarationElement {

	private final ASTSingle<Expression> initializer;
	
	public InitializerVariableDeclarationElement(Context context, VarNameDeclaration name, int numDims, Expression initializer) {
		super(context, name, numDims);

		this.initializer = initializer != null ? makeSingle(initializer) : null;
	}
	
	public final Expression getInitializer() {
		return initializer != null ? initializer.get() : null;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INITIALIZER_VARIABLE_DECLARATION_ELEMENT;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		super.doRecurse(recurseMode, iterator);
		
		if (initializer != null) {
			doIterate(initializer, recurseMode, iterator);
		}
	}
}
