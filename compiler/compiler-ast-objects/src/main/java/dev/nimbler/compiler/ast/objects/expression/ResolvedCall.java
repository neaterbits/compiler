package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.block.CallableName;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.ResolvedPrimary;

public abstract class ResolvedCall<N extends CallableName> extends ResolvedPrimary {

	private final ASTSingle<N> callable;
	private final ASTSingle<ParameterList> parameters;

	protected ResolvedCall(Context context, N callable, ParameterList parameters) {
		super(context);

		Objects.requireNonNull(callable);
		Objects.requireNonNull(parameters);

		this.callable = makeSingle(callable);
		this.parameters = makeSingle(parameters);
	}

	public final N getCallable() {
		return callable.get();
	}

	public final ParameterList getParameters() {
		return parameters.get();
	}
	
	@Override
	public final TypeReference getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(callable, recurseMode, iterator);
		
		doIterate(parameters, recurseMode, iterator);
	}
}
