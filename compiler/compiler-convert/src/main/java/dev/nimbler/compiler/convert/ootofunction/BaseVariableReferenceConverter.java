package dev.nimbler.compiler.convert.ootofunction;

import dev.nimbler.compiler.ast.objects.expression.ArrayAccessExpression;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.variables.ArrayAccessReference;
import dev.nimbler.compiler.ast.objects.variables.FieldAccessReference;
import dev.nimbler.compiler.ast.objects.variables.NameReference;
import dev.nimbler.compiler.ast.objects.variables.PrimaryListVariableReference;
import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.convert.ConverterState;
import dev.nimbler.compiler.convert.VariableReferenceConverter;

public abstract class BaseVariableReferenceConverter<T extends ConverterState<T>>
		extends BaseConverter<T>
		implements VariableReferenceConverter<T> {

	@Override
	public final VariableReference onNameReference(NameReference nameReference, T param) {
		return new NameReference(nameReference.getContext(), nameReference.getName());
	}

	@Override
	public final VariableReference onArrayAccessReference(ArrayAccessReference variableReference, T param) {

		return new ArrayAccessReference(
				variableReference.getContext(),
				(ArrayAccessExpression)convertExpression(variableReference.getExpression(), param));
	}

	@Override
	public final VariableReference onFieldAccessReference(FieldAccessReference fieldAccessReference, T param) {

		return new FieldAccessReference(
				fieldAccessReference.getContext(),
				(FieldAccess)convertExpression(fieldAccessReference.getExpression(), param));
	}

	@Override
	public final VariableReference onPrimaryList(PrimaryListVariableReference primaryListVariableReference, T param) {

		return new PrimaryListVariableReference(
				primaryListVariableReference.getContext(),
				(PrimaryList)convertExpression(primaryListVariableReference.getList(), param));
	}
}
