package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.Keyword;
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
			Keyword interfaceKeyword,
			InterfaceDeclarationName name,
			List<TypeReference> extendsInterfaces,
			List<ComplexMemberDefinition> members) {
		
		super(context, interfaceKeyword, name, members);

		this.modifiers = makeSingle(modifiers);
		this.extendsInterfaces = extendsInterfaces != null ? makeList(extendsInterfaces) : null;
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
		doIterateTypeKeywordAndName(recurseMode, iterator);
		
		if (extendsInterfaces != null) {
			doIterate(extendsInterfaces, recurseMode, iterator);
		}
		
		super.doRecurse(recurseMode, iterator);
	}
}
