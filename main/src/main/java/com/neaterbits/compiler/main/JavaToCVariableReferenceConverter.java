package com.neaterbits.compiler.main;

import java.util.Arrays;

import com.neaterbits.compiler.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.expression.FieldAccess;
import com.neaterbits.compiler.ast.expression.PrimaryList;
import com.neaterbits.compiler.ast.parser.FieldAccessType;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.typedefinition.FieldName;
import com.neaterbits.compiler.ast.variables.PrimaryListVariableReference;
import com.neaterbits.compiler.ast.variables.StaticMemberReference;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.convert.ootofunction.BaseVariableReferenceConverter;
import com.neaterbits.compiler.util.Context;

final class JavaToCVariableReferenceConverter<T extends MappingJavaToCConverterState<T>> extends BaseVariableReferenceConverter<T> {

	@Override
	public VariableReference onStaticMemberReference(StaticMemberReference staticMemberReference, T param) {
		
		final NamedType namedType = (NamedType)staticMemberReference.getClassType().getType();
		
		final int typeNo = param.getTypeNo(namedType.getCompleteName().toTypeName());
		
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
