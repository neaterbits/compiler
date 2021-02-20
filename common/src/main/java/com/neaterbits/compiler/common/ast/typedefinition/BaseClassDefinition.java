package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public abstract class BaseClassDefinition extends ComplexTypeDefinition {

	private final ASTSingle<ClassModifiers> modifiers;
	
	protected BaseClassDefinition(Context context, ClassModifiers modifiers, ClassName name, List<ComplexMemberDefinition> members) {
		super(context, name, members);

		this.modifiers = makeSingle(modifiers);
	}

	public ClassModifiers getModifiers() {
		return modifiers.get();
	}
	
	public final ClassMethodMember findMethod(String methodName) {
		
		return (ClassMethodMember)getMembers().find(member -> 
							   member instanceof ClassMethodMember
							&& ((ClassMethodMember)member).getMethod().getName().getName().equals(methodName));
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
