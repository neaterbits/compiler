package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.util.parse.context.Context;

public abstract class DataFieldMember extends ComplexMemberDefinition {

	private final ASTSingle<TypeReference> type;

	public DataFieldMember(Context context, TypeReference type) {
		super(context);
		
		Objects.requireNonNull(type);
	
		this.type = makeSingle(type);
	}

	public final TypeReference getType() {
		return type.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(type, recurseMode, iterator);
	}
}
