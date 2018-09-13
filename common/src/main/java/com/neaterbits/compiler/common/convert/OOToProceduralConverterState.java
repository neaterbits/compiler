package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;

public abstract class OOToProceduralConverterState extends ConverterState {

	public abstract FunctionName methodToFunctionName(Namespace namespace, MethodName methodName);

	public abstract StructName classToStructName(Namespace namespace, ClassName className);
	
	
}
