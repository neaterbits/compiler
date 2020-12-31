package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typedefinition.FieldName;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.ResolvedPrimary;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.util.parse.context.Context;

public final class FieldAccess extends ResolvedPrimary {

	private final FieldAccessType type;
	private final ASTSingle<TypeReference> classType;
	private final FieldName fieldName;
	
	public FieldAccess(Context context, FieldAccessType type, TypeReference classType, FieldName fieldName) {
		super(context);
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(fieldName);
		
		this.type = type;
		this.classType = classType != null ? makeSingle(classType) : null;
		this.fieldName = fieldName;
	}

	public FieldAccessType getFieldAccessType() {
		return type;
	}

	public TypeReference getClassType() {
		return classType.get();
	}

	public FieldName getFieldName() {
		return fieldName;
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_ACCESS;
	}

	@Override
	public TypeReference getType() {
		
		throw new UnsupportedOperationException();
		
		// final ComplexType<?, ?, ?> type = (ComplexType<?, ?, ?>)classType.get().getType();

		// return type.getFieldType(fieldName);
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFieldAccess(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		if (classType != null) {
			doIterate(classType, recurseMode, iterator);
		}
	}
}
