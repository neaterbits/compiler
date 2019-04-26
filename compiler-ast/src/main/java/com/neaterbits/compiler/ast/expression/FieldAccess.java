package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.parser.FieldAccessType;
import com.neaterbits.compiler.ast.typedefinition.FieldName;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class FieldAccess extends Primary {

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
