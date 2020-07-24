package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
