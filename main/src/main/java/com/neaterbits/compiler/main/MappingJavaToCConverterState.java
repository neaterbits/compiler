package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;
import com.neaterbits.compiler.common.convert.Converters;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.common.util.Strings;

public class MappingJavaToCConverterState<T extends MappingJavaToCConverterState<T>> extends OOToProceduralConverterState<T> {

	public MappingJavaToCConverterState(Converters<T> converters) {
		super(converters);
	}
	
	@Override
	public final FunctionName methodToFunctionName(NamespaceReference namespace, MethodName methodName) {
		return new FunctionName(Strings.join(namespace.getParts(), '_') + '_' + methodName.getName());
	}

	@Override
	public final StructName classToStructName(NamespaceReference namespace, ClassName className) {
		return new StructName(Strings.join(namespace.getParts(), '_') + '_' + className.getName());
	}
}
