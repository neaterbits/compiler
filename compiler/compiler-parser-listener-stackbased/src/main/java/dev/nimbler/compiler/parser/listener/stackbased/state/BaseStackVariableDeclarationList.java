package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
