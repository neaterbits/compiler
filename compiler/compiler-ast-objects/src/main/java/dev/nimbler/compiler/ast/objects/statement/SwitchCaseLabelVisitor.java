package dev.nimbler.compiler.ast.objects.statement;

public interface SwitchCaseLabelVisitor<T, R> {

	R onConstant(ConstantSwitchCaseLabel label, T param);
	R onEnum(EnumSwitchCaseLabel label, T param);
	R onDefault(DefaultSwitchCaseLabel label, T param);
	
}
