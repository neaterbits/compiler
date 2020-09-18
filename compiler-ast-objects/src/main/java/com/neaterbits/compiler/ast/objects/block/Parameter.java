package com.neaterbits.compiler.ast.objects.block;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.name.Name;

public final class Parameter extends BaseASTElement {

    private final ASTSingle<ParameterModifiers> modifiers;
	private final ASTSingle<TypeReference> type;
	private final ASTSingle<ParameterName> name;
	private final boolean varArgs;

	public Parameter(Context context, ParameterModifiers modifiers, TypeReference type, ParameterName name, boolean varArgs) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		Name.check(name.getName());

		this.modifiers = makeSingle(modifiers);
		this.type = makeSingle(type);
		this.name = makeSingle(name);
		this.varArgs = varArgs;
	}
	
	public ParameterModifiers getModifiers() {
        return modifiers.get();
    }

    public TypeReference getType() {
		return type.get();
	}
	
	public ParameterName getName() {
		return name.get();
	}

	public String getNameString() {
        return name.get().getName();
    }

	public boolean isVarArgs() {
		return varArgs;
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PARAMETER;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(type, recurseMode, iterator);
		doIterate(name, recurseMode, iterator);
	}
}
