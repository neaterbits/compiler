package com.neaterbits.compiler.ast.parser.stackstate;

import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.parser.TypeReferenceSetter;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class BaseStackVariableDeclarationList
			extends ListStackEntry<InitializerVariableDeclarationElement>
			implements TypeReferenceSetter {

	private TypeReference typeReference;

	public BaseStackVariableDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public final void setTypeReference(TypeReference typeReference) {
		if (this.typeReference != null) {
			throw new IllegalStateException("typeReference already set");
		}
		
		if (getParseLogger() != null) {
			getParseLogger().onStackSetElement(typeReference.toString());
		}

		this.typeReference = typeReference;
	}

	public TypeReference getTypeReference() {
		return typeReference;
	}
}
