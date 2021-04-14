package dev.nimbler.compiler.c.emit;

import dev.nimbler.compiler.ast.objects.statement.ConstantSwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.DefaultSwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.EnumSwitchCaseLabel;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.SwitchCaseLabelEmitter;

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
