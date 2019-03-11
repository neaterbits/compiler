package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class InterfaceDefinition extends ComplexTypeDefinition<InterfaceName, InterfaceDeclarationName> {

	private final ASTSingle<InterfaceModifiers> modifiers;

	private final ASTList<TypeReference> extendsInterfaces;
	
	public InterfaceDefinition(
			Context context,
			InterfaceModifiers modifiers,
			InterfaceDeclarationName name,
			List<TypeReference> extendsInterfaces,
			List<ComplexMemberDefinition> members) {
		
		super(context, name, members);

		this.modifiers = makeSingle(modifiers);
		this.extendsInterfaces = makeList(extendsInterfaces);
	}

	public InterfaceModifiers getModifiers() {
		return modifiers.get();
	}
	
	public ASTList<TypeReference> getExtendsInterfaces() {
		return extendsInterfaces;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInterfaceDefinition(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(modifiers, recurseMode, iterator);
		doIterate(extendsInterfaces, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}