package dev.nimbler.compiler.language.java.emit;

import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.statement.ConstantSwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.DefaultSwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.EnumSwitchCaseLabel;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.SwitchCaseLabelEmitter;

final class JavaSwitchCaseLabelEmitter implements SwitchCaseLabelEmitter<EmitterState> {

	private static final JavaExpressionEmitter EXPRESSION_EMITTER = new JavaExpressionEmitter();
	
	private void emitExpression(Expression expression, EmitterState param) {
		expression.visit(EXPRESSION_EMITTER, param);
	}
	
	@Override
	public Void onConstant(ConstantSwitchCaseLabel label, EmitterState param) {

		emitExpression(label.getConstant(), param);

		param.append(':');

		return null;
	}

	@Override
	public Void onEnum(EnumSwitchCaseLabel label, EmitterState param) {

		param.append(label.getEnumConstant()).append(':');

		return null;
	}

	@Override
	public Void onDefault(DefaultSwitchCaseLabel label, EmitterState param) {
		param.append("default:");
		
		return null;
	}
}
