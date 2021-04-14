package dev.nimbler.compiler.main;

import java.util.Arrays;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.ArrayAccessExpression;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldName;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.PrimaryListVariableReference;
import dev.nimbler.compiler.ast.objects.variables.StaticMemberReference;
import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.convert.ootofunction.BaseVariableReferenceConverter;
import dev.nimbler.compiler.util.parse.FieldAccessType;

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
