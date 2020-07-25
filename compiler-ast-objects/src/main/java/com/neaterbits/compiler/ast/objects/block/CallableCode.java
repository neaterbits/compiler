package com.neaterbits.compiler.ast.objects.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.generics.NamedGenericTypeParameter;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class CallableCode<NAME extends CallableName> extends Callable<NAME>  {

	private final ASTSingle<Block> block;
	private final ASTList<NamedGenericTypeParameter> genericTypes;
	
	CallableCode(Context context, List<NamedGenericTypeParameter> genericTypes, TypeReference returnType, NAME name, List<Parameter> parameters, Block block) {
		super(context, returnType, name, parameters);
		
		Objects.requireNonNull(block);

		this.genericTypes = genericTypes != null ? makeList(genericTypes) : null;
		this.block = makeSingle(block);
	}
	
	public final ASTList<NamedGenericTypeParameter> getGenericTypes() {
        return genericTypes;
    }

    public final Block getBlock() {
		return block.get();
	}


	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		super.doRecurse(recurseMode, iterator);
		
		if (genericTypes != null) {
		    doIterate(genericTypes, recurseMode, iterator);
		}
		
		doIterate(block, recurseMode, iterator);
	}
}
