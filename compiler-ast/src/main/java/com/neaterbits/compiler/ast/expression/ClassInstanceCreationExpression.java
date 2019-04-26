package com.neaterbits.compiler.ast.expression;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassInstanceCreationExpression extends Call<ConstructorName> {

	private final ASTSingle<TypeReference> type;
	private final ASTList<ClassMethodMember> anonymousClassMethods;

	public ClassInstanceCreationExpression(Context context, TypeReference type, ConstructorName name, ParameterList parameters, List<ClassMethodMember> anonymousClassMethods) {
		super(context, name, parameters);

		Objects.requireNonNull(type);
		Objects.requireNonNull(parameters);

		this.type = makeSingle(type);
		this.anonymousClassMethods = makeList(anonymousClassMethods);
	}

	public TypeReference getTypeReference() {
		return type.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onClassInstanceCreation(this, param);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(type, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
		
		doIterate(anonymousClassMethods, recurseMode, iterator);
	}
}
