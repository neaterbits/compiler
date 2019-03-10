package com.neaterbits.compiler.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class DataFieldMember extends ComplexMemberDefinition {

	private final ASTSingle<TypeReference> type;
	private final FieldName name;

	public DataFieldMember(Context context, TypeReference type, FieldName name) {
		super(context);
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
	
		this.type = makeSingle(type);
		this.name = name;
	}

	public final TypeReference getType() {
		return type.get();
	}

	public final FieldName getName() {
		return name;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(type, recurseMode, iterator);
	}
}
