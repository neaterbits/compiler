package com.neaterbits.compiler.common.ast.block;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.statement.Statement;

public abstract class Block extends BaseASTElement {
	
	private final List<Statement> statements;
	
	public Block(Context context, List<Statement> statements) {
		super(context);
		
		Objects.requireNonNull(statements);
		
		this.statements = Collections.unmodifiableList(statements);
	}

	public final List<Statement> getStatements() {
		return statements;
	}
}
