package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;

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
