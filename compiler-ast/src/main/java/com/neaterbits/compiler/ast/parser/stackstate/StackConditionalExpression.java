package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackConditionalExpression extends StackEntry {

	private Expression part1;
	private Expression part2;
	private Expression part3;

	public StackConditionalExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public Expression getPart1() {
		return part1;
	}

	public void setPart1(Expression part1) {

		Objects.requireNonNull(part1);
		
		this.part1 = part1;
	}

	public Expression getPart2() {
		return part2;
	}

	public void setPart2(Expression part2) {
		
		Objects.requireNonNull(part2);
		
		this.part2 = part2;
	}

	public Expression getPart3() {
		return part3;
	}

	public void setPart3(Expression part3) {
		
		Objects.requireNonNull(part3);
		
		this.part3 = part3;
	}
}
