package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.variables.StaticMemberReference;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.convert.ootofunction.BaseVariableReferenceConverter;

final class JavaToCVariableReferenceConverter<T extends MappingJavaToCConverterState<T>> extends BaseVariableReferenceConverter<T> {

	@Override
	public VariableReference onStaticMemberReference(StaticMemberReference staticMemberReference, T param) {
		throw new UnsupportedOperationException();
	}
	
}
