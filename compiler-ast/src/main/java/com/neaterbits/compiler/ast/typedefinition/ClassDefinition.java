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

public final class ClassDefinition extends BaseClassDefinition {
	
	private final ASTSingle<Keyword> extendsKeyword;
	private final ASTList<TypeReference> extendsClasses;
	
	public ClassDefinition(Context context, ClassModifiers modifiers, Keyword classKeyword, ClassDeclarationName name,
			Keyword extendsKeyword, List<TypeReference> extendsClasses,
			List<TypeReference> implementsInterfaces,
			List<ComplexMemberDefinition> members) {
		super(context, modifiers, classKeyword, name, implementsInterfaces, members);
		
		this.extendsKeyword = extendsKeyword != null ? makeSingle(extendsKeyword) : null;
		this.extendsClasses = extendsClasses != null ? makeList(extendsClasses) : null;
	}

	public Keyword getExtendsKeyword() {
		return extendsKeyword.get();
	}

	public ASTList<TypeReference> getExtendsClasses() {
		return extendsClasses;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassDefinition(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterateModifiersAndName(recurseMode, iterator);
		
		if (extendsKeyword != null) {
			doIterate(extendsKeyword, recurseMode, iterator);
		}
		
		if (extendsClasses != null) {
			doIterate(extendsClasses, recurseMode, iterator);
		}
		
		doIterateImplementsInterfaces(recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
