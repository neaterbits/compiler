package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class InnerClassMember extends ComplexMemberDefinition {

	private final ASTSingle<ClassDefinition> classDefinition;
	
	public InnerClassMember(Context context, ClassDefinition classDefinition) {
		super(context);
		
		Objects.requireNonNull(classDefinition);
		
		this.classDefinition = makeSingle(classDefinition);
	}

	public ClassDefinition getClassDefinition() {
		return classDefinition.get();
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.INNER_CLASS;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INNER_CLASS_MEMBER;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInnerClassMember(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(classDefinition, recurseMode, iterator);
	}
}
