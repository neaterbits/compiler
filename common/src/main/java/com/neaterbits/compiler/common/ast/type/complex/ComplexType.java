package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.TypeName;

public abstract class ComplexType extends NamedType {

	public ComplexType(NamespaceReference namespace, TypeName name, boolean nullable) {
		super(namespace, name, nullable);
	}
}
