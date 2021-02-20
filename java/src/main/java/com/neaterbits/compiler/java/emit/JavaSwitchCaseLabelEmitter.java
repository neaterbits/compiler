package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.ConstantSwitchCaseLabel;
import com.neaterbits.compiler.common.ast.statement.DefaultSwitchCaseLabel;
import com.neaterbits.compiler.common.ast.statement.EnumSwitchCaseLabel;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.SwitchCaseLabelEmitter;

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
