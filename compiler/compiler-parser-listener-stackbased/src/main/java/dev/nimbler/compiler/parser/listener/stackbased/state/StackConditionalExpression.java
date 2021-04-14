package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class StackConditionalExpression<EXPRESSION> extends StackEntry {

	private EXPRESSION part1;
	private EXPRESSION part2;
	private EXPRESSION part3;

	public StackConditionalExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public EXPRESSION getPart1() {
		return part1;
	}

	public void setPart1(EXPRESSION part1) {

		Objects.requireNonNull(part1);
		
		this.part1 = part1;
	}

	public EXPRESSION getPart2() {
		return part2;
	}

	public void setPart2(EXPRESSION part2) {
		
		Objects.requireNonNull(part2);
		
		this.part2 = part2;
	}

	public EXPRESSION getPart3() {
		return part3;
	}

	public void setPart3(EXPRESSION part3) {
		
		Objects.requireNonNull(part3);
		
		this.part3 = part3;
	}
}
