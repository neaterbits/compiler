package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.generics.TypeArgument;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassDefinition extends BaseClassDefinition {
	
    private final ASTList<TypeArgument> genericTypes;
	private final ASTSingle<Keyword> extendsKeyword;
	private final ASTList<TypeReference> extendsClasses;
	
	public ClassDefinition(Context context, ClassModifiers modifiers, Keyword classKeyword, ClassDeclarationName name,
	        List<TypeArgument> genericTypes,
			Keyword extendsKeyword, List<TypeReference> extendsClasses,
			List<TypeReference> implementsInterfaces,
			List<ComplexMemberDefinition> members) {
		super(context, modifiers, classKeyword, name, implementsInterfaces, members);
		
		this.genericTypes = genericTypes != null ? makeList(genericTypes) : null;
		
		this.extendsKeyword = extendsKeyword != null ? makeSingle(extendsKeyword) : null;
		this.extendsClasses = extendsClasses != null ? makeList(extendsClasses) : null;
	}

	public ASTList<TypeArgument> getGenericTypes() {
        return genericTypes;
    }

    public Keyword getExtendsKeyword() {
		return extendsKeyword.get();
	}

	public ASTList<TypeReference> getExtendsClasses() {
		return extendsClasses;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_DEFINITION;
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
