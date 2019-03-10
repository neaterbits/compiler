package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class BaseClassDefinition extends ComplexTypeDefinition<ClassName, ClassDeclarationName> {

	private final ASTSingle<ClassModifiers> modifiers;
	
	private final ASTList<TypeReference> implementsInterfaces;
	
	protected BaseClassDefinition(
			Context context,
			ClassModifiers modifiers,
			ClassDeclarationName name,
			List<TypeReference> implementsInterfaces, List<ComplexMemberDefinition> members) {
		super(context, name, members);

		this.modifiers = makeSingle(modifiers);
		this.implementsInterfaces = makeList(implementsInterfaces);
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

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		doIterate(implementsInterfaces, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
