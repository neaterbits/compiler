package com.neaterbits.compiler.c.emit;

import com.neaterbits.compiler.common.ast.statement.ConstantSwitchCaseLabel;
import com.neaterbits.compiler.common.ast.statement.DefaultSwitchCaseLabel;
import com.neaterbits.compiler.common.ast.statement.EnumSwitchCaseLabel;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.SwitchCaseLabelEmitter;

final class CSwitchCaseLabelEmitter implements SwitchCaseLabelEmitter<EmitterState> {

	private static final CExpressionEmitter EXPRESSION_EMITTER = new CExpressionEmitter();
	
	@Override
	public Void onConstant(ConstantSwitchCaseLabel label, EmitterState param) {

		label.getConstant().visit(EXPRESSION_EMITTER, param);
		
		param.append(':');
		
		return null;
	}

	@Override
	public Void onEnum(EnumSwitchCaseLabel label, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onDefault(DefaultSwitchCaseLabel label, EmitterState param) {
		
		param.append("default:");
		
		return null;
	}
}
