package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldName;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.ResolvedPrimary;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.util.parse.FieldAccessType;

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
