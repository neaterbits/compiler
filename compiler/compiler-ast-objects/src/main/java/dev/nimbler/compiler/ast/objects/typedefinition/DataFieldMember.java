package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;

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
