package dev.nimbler.compiler.ast.objects.statement;

import dev.nimbler.compiler.ast.objects.block.ConstructorInvocationStatement;

public interface StatementVisitor<T, R> {

	R onIf(IfElseIfElseStatement statement, T param);

	R onSwitchCase(SwitchCaseStatement statement, T param);
	
	R onWhile(WhileStatement statement, T param);

	R onDoWhile(DoWhileStatement statement, T param);

	R onCFor(ForStatement statement, T param);
	
	R onIteratorFor(IteratorForStatement statement, T param);

	R onTryCatchFinallyStatement(TryCatchFinallyStatement statement, T param);
	
	R onTryWithResourcesStatement(TryWithResourcesStatement statement, T param);
	
	R onThrowStatement(ThrowStatement statement, T param);
	
	R onAssignment(AssignmentStatement statement, T param);

	R onVariableDeclaration(VariableDeclarationStatement statement, T param);

	R onExpressionStatement(ExpressionStatement statement, T param);

	R onReturnStatement(ReturnStatement statement, T param);
	
	R onBreakStatement(BreakStatement statement, T param);
	
	R onConstructorInvocation(ConstructorInvocationStatement statement, T param);
}
