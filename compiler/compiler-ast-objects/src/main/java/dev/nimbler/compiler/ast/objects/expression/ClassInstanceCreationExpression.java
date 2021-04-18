package dev.nimbler.compiler.ast.objects.expression;

import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorName;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ClassInstanceCreationExpression extends ResolvedCall<ConstructorName> {

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
