package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;

public abstract class OOToProceduralConverterState<T extends OOToProceduralConverterState<T>>
			extends ConverterState<T> {

	public abstract FunctionName methodToFunctionName(NamespaceReference namespace, MethodName methodName);

	public abstract StructName classToStructName(NamespaceReference namespace, ClassName className);

	protected OOToProceduralConverterState(Converters<T> converters) {
		
		super(converters);
	}
}
