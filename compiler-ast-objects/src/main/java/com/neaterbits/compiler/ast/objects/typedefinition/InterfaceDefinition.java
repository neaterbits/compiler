package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_DEFINITION;
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
