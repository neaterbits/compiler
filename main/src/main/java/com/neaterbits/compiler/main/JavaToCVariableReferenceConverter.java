package com.neaterbits.compiler.main;

import java.util.Arrays;

import com.neaterbits.compiler.ast.objects.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.objects.expression.FieldAccess;
import com.neaterbits.compiler.ast.objects.expression.PrimaryList;
import com.neaterbits.compiler.ast.objects.typedefinition.FieldName;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.PrimaryListVariableReference;
import com.neaterbits.compiler.ast.objects.variables.StaticMemberReference;
import com.neaterbits.compiler.ast.objects.variables.VariableReference;
import com.neaterbits.compiler.convert.ootofunction.BaseVariableReferenceConverter;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.util.parse.context.Context;

final class JavaToCVariableReferenceConverter<T extends MappingJavaToCConverterState<T>> extends BaseVariableReferenceConverter<T> {

	@Override
	public VariableReference onStaticMemberReference(StaticMemberReference staticMemberReference, T param) {
		
		final TypeReference namedType = staticMemberReference.getClassType();
		
		final int typeNo = param.getTypeNo(namedType.getTypeName());
		
		final Context context = staticMemberReference.getContext();
		
		final ArrayAccessExpression arrayAccessExpression = makeStaticArrayAccess(
				context,
				param.getClassStaticMembersArrayName(),
				typeNo,
				param);

		final PrimaryList primaryList = new PrimaryList(
				context,
				Arrays.asList(
						arrayAccessExpression,
						new FieldAccess(
								context,
								FieldAccessType.FIELD,
								staticMemberReference.getClassType(),
								new FieldName(staticMemberReference.getName()))
						)
				);

		return new PrimaryListVariableReference(context, primaryList);
	}
}
