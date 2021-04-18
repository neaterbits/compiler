package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ClassDataFieldMember extends DataFieldMember {

	private final ASTSingle<FieldModifiers> modifiers;
	private final ASTList<InitializerVariableDeclarationElement> initializers;

	public ClassDataFieldMember(
	        Context context,
	        FieldModifiers modifiers,
	        TypeReference type,
	        List<InitializerVariableDeclarationElement> initializers) {
	    
		super(context, type);
		
		Objects.requireNonNull(initializers);

		this.modifiers = makeSingle(modifiers);
		this.initializers = makeList(initializers);
	}

	public ASTList<InitializerVariableDeclarationElement> getInitializers() {
		return initializers;
	}
	
	public InitializerVariableDeclarationElement getInitializer(int index) {
	    return initializers.get(index);
	}

	public FieldModifiers getModifiers() {
		return modifiers.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_INSTANCE_CREATION_EXPRESSION;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassDataFieldMember(this, param);
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.FIELD;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	    doIterate(modifiers, recurseMode, iterator);

	    super.doRecurse(recurseMode, iterator);

        doIterate(initializers, recurseMode, iterator);
	}
}
