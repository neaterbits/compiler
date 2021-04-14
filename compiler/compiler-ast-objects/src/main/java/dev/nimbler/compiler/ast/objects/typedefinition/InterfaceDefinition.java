package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

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
