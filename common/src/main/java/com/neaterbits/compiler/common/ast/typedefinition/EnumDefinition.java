package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class EnumDefinition extends BaseClassDefinition {

	private final ASTList<EnumConstantDefinition> constants;
	
	public EnumDefinition(Context context, ClassModifiers modifiers, ClassName name,
			List<EnumConstantDefinition> constants,
			List<ComplexMemberDefinition> members) {
		super(context, modifiers, name, members);
	
		Objects.requireNonNull(constants);
		
		this.constants = makeList(constants);
	}

	public ASTList<EnumConstantDefinition> getConstants() {
		return constants;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onEnumDefinition(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(constants, recurseMode, visitor);

		super.doRecurse(recurseMode, visitor);
	}
}
