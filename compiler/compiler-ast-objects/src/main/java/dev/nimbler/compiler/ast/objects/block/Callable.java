package dev.nimbler.compiler.ast.objects.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;

public abstract class Callable<NAME extends CallableName> extends CompilationCode {

	private final ASTSingle<TypeReference> returnType;
	private final ASTSingle<NAME> name;
	private final ASTList<Parameter> parameters;

	protected Callable(Context context, TypeReference returnType, NAME name, List<Parameter> parameters) {
		super(context);

		Objects.requireNonNull(name);
		Objects.requireNonNull(parameters);

		this.returnType = returnType != null ? makeSingle(returnType) : null;
		this.name = makeSingle(name);
		this.parameters = makeList(parameters);
	}

	public final TypeReference getReturnType() {
		return returnType != null ? returnType.get() : null;
	}

	public final NAME getName() {
		return name.get();
	}

	public final String getNameString() {
	    return name.get().getName();
	}
	
	public final ASTList<Parameter> getParameters() {
		return parameters;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		if (returnType != null) {
			doIterate(returnType, recurseMode, iterator);
		}
		
		doIterate(name, recurseMode, iterator);

		doIterate(parameters, recurseMode, iterator);
	}
}
