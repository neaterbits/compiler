package com.neaterbits.compiler.main;

import java.util.Arrays;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.common.ast.expression.FieldAccess;
import com.neaterbits.compiler.common.ast.expression.PrimaryList;
import com.neaterbits.compiler.common.ast.typedefinition.FieldName;
import com.neaterbits.compiler.common.ast.variables.PrimaryListVariableReference;
import com.neaterbits.compiler.common.ast.variables.StaticMemberReference;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.convert.ootofunction.BaseVariableReferenceConverter;
import com.neaterbits.compiler.common.parser.FieldAccessType;

final class JavaToCVariableReferenceConverter<T extends MappingJavaToCConverterState<T>> extends BaseVariableReferenceConverter<T> {

	@Override
	public VariableReference onStaticMemberReference(StaticMemberReference staticMemberReference, T param) {
		
		final int typeNo = param.getTypeNo(staticMemberReference.getClassType().getType());
		
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
