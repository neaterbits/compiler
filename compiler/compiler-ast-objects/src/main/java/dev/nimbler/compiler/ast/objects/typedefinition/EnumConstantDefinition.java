package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.expression.ParameterList;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class EnumConstantDefinition extends CompilationCode  {

	private final EnumConstantName name;
	private final ASTSingle<ParameterList> parameters;
	
	private final ASTList<ComplexMemberDefinition> members;
	
	public EnumConstantDefinition(Context context, EnumConstantName name, ParameterList parameters, List<ComplexMemberDefinition> members) {
		super(context);

		Objects.requireNonNull(name);
		Objects.requireNonNull(members);

		this.name = name;
		this.parameters = parameters != null ? makeSingle(parameters) : null;
		this.members = makeList(members);
	}

	public EnumConstantName getName() {
		return name;
	}
	
	public String getNameString() {
	    return name.getName();
	}

	public ParameterList getParameters() {
		return parameters.get();
	}

	public ASTList<ComplexMemberDefinition> getMembers() {
		return members;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ENUM_CONSTANT_DEFINITION;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onEnumConstantDefinition(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(members, recurseMode, iterator);
	}
}
