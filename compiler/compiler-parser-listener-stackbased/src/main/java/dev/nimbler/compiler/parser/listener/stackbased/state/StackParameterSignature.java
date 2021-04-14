package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackParameterSignature<ANNOTATION, VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE>
	extends BaseStackVariableDeclaration<ANNOTATION, VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE>
	implements TypeReferenceSetter<TYPE_REFERENCE>, AnnotationSetter<ANNOTATION> {

	private boolean varargs;
	
	public StackParameterSignature(ParseLogger parseLogger, boolean varargs) {
		super(parseLogger);

		this.varargs = varargs;
	}
	
	public void setVarargs() {
	    this.varargs = true;
	}

	public boolean isVarargs() {
		return varargs;
	}
}
