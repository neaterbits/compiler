package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public abstract class DataFieldMember extends ComplexMemberDefinition {

	private final ASTSingle<TypeReference> type;
	private final String name;

	public DataFieldMember(Context context, TypeReference type, String name) {
		super(context);
	
		this.type = makeSingle(type);
		this.name = name;
	}

	public final TypeReference getType() {
		return type.get();
	}

	public final String getName() {
		return name;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(type, recurseMode, visitor);
	}
}
