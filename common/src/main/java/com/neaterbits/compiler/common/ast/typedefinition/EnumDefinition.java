package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class EnumDefinition extends BaseClassDefinition {

	private final ASTList<EnumConstantDefinition> constants;
	
	public EnumDefinition(Context context, ClassModifiers modifiers, ClassName name,
			List<TypeReference> implementsInterfaces,
			List<EnumConstantDefinition> constants,
			List<ComplexMemberDefinition> members) {
		super(context, modifiers, name, implementsInterfaces, members);
	
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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(constants, recurseMode, iterator);

		super.doRecurse(recurseMode, iterator);
	}
}
