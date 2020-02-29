package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.TypeReferenceSetter;

public abstract class BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION>
			extends ListStackEntry<StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION>>
			implements TypeReferenceSetter<TYPE_REFERENCE> {

	private TYPE_REFERENCE typeReference;

	public BaseStackVariableDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public final void setTypeReference(TYPE_REFERENCE typeReference) {
		if (this.typeReference != null) {
			throw new IllegalStateException("typeReference already set");
		}
		
		if (getParseLogger() != null) {
			getParseLogger().onStackSetElement(typeReference.toString());
		}

		this.typeReference = typeReference;
	}

	public TYPE_REFERENCE getTypeReference() {
		return typeReference;
	}
}
