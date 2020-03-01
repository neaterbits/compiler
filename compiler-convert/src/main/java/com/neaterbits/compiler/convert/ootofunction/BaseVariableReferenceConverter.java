package com.neaterbits.compiler.convert.ootofunction;

import com.neaterbits.compiler.ast.objects.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.objects.expression.FieldAccess;
import com.neaterbits.compiler.ast.objects.expression.PrimaryList;
import com.neaterbits.compiler.ast.objects.variables.ArrayAccessReference;
import com.neaterbits.compiler.ast.objects.variables.FieldAccessReference;
import com.neaterbits.compiler.ast.objects.variables.NameReference;
import com.neaterbits.compiler.ast.objects.variables.PrimaryListVariableReference;
import com.neaterbits.compiler.ast.objects.variables.SimpleVariableReference;
import com.neaterbits.compiler.ast.objects.variables.VariableReference;
import com.neaterbits.compiler.convert.ConverterState;
import com.neaterbits.compiler.convert.VariableReferenceConverter;

public abstract class BaseVariableReferenceConverter<T extends ConverterState<T>>
		extends BaseConverter<T>
		implements VariableReferenceConverter<T> {

	@Override
	public final VariableReference onNameReference(NameReference nameReference, T param) {
		return new NameReference(nameReference.getContext(), nameReference.getName());
	}

	@Override
	public final VariableReference onSimpleVariableReference(SimpleVariableReference variableReference, T param) {

		return new SimpleVariableReference(
				variableReference.getContext(),
				mapVariableDeclaration(variableReference.getDeclaration(), param));
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
