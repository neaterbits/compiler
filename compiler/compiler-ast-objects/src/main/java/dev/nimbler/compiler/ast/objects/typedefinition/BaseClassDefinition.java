package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.util.name.ClassName;

public abstract class BaseClassDefinition extends ComplexTypeDefinition<ClassName, ClassDeclarationName> {

	private final ASTSingle<ClassModifiers> modifiers;
	
	private final ASTList<TypeReference> implementsInterfaces;
	
	protected BaseClassDefinition(
			Context context,
			ClassModifiers modifiers,
			Keyword typeKeyword,
			ClassDeclarationName name,
			List<TypeReference> implementsInterfaces, List<ComplexMemberDefinition> members) {
		super(context, typeKeyword, name, members);

		this.modifiers = makeSingle(modifiers);
		this.implementsInterfaces = implementsInterfaces != null ? makeList(implementsInterfaces) : null;
	}

	public ClassModifiers getModifiers() {
		return modifiers.get();
	}
	
	public final ASTList<TypeReference> getImplementsInterfaces() {
		return implementsInterfaces;
	}

	public final ClassMethodMember findMethod(String methodName) {
		
		return (ClassMethodMember)getMembers().find(member -> 
							   member instanceof ClassMethodMember
							&& ((ClassMethodMember)member).getMethod().getName().getName().equals(methodName));
	}

	protected final void doIterateModifiersAndName(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(modifiers, recurseMode, iterator);
		doIterateTypeKeywordAndName(recurseMode, iterator);
		
	}

	protected final void doIterateImplementsInterfaces(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		if (implementsInterfaces != null) {
			doIterate(implementsInterfaces, recurseMode, iterator);
		}
	}
}
