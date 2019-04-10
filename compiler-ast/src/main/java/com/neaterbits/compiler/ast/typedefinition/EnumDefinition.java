package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class EnumDefinition extends BaseClassDefinition {

	private final ASTList<EnumConstantDefinition> constants;
	
	public EnumDefinition(
			Context context,
			ClassModifiers modifiers,
			Keyword enumKeyword,
			ClassDeclarationName name,
			List<TypeReference> implementsInterfaces,
			List<EnumConstantDefinition> constants,
			List<ComplexMemberDefinition> members) {
		super(context, modifiers, enumKeyword, name, implementsInterfaces, members);
	
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

		doIterateModifiersAndName(recurseMode, iterator);
		doIterateImplementsInterfaces(recurseMode, iterator);
		
		doIterate(constants, recurseMode, iterator);

		super.doRecurse(recurseMode, iterator);
	}
}
