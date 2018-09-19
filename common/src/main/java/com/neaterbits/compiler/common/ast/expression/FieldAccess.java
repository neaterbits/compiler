package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.typedefinition.FieldName;
import com.neaterbits.compiler.common.parser.FieldAccessType;

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

	public FieldAccessType getType() {
		return type;
	}

	public TypeReference getClassType() {
		return classType.get();
	}

	public FieldName getFieldName() {
		return fieldName;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFieldAccess(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(classType, recurseMode, visitor);
	}
}
