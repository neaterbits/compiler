package dev.nimbler.compiler.ast.objects.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.generics.NamedGenericTypeParameter;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;

public abstract class CallableCode<NAME extends CallableName> extends Callable<NAME>  {

	private final ASTSingle<Block> block;
	private final ASTList<NamedGenericTypeParameter> genericTypes;
	private final ASTList<TypeReference> thrownExceptions;
	
	CallableCode(
	        Context context,
	        List<NamedGenericTypeParameter> genericTypes,
	        TypeReference returnType,
	        NAME name,
	        List<Parameter> parameters,
	        List<TypeReference> thrownExceptions,
	        Block block) {
	    
		super(context, returnType, name, parameters);
		
		Objects.requireNonNull(block);

		this.genericTypes = genericTypes != null ? makeList(genericTypes) : null;
		this.thrownExceptions = thrownExceptions != null ? makeList(thrownExceptions) : null;
		this.block = makeSingle(block);
	}
	
	public final ASTList<NamedGenericTypeParameter> getGenericTypes() {
        return genericTypes;
    }

    public final ASTList<TypeReference> getThrownExceptions() {
        return thrownExceptions;
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

		if (thrownExceptions != null) {
		    doIterate(thrownExceptions, recurseMode, iterator);
		}
		
		doIterate(block, recurseMode, iterator);
	}
}
