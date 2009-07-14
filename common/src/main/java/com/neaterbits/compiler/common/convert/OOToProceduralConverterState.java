package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;

public abstract class OOToProceduralConverterState<T extends OOToProceduralConverterState<T>>
			extends ConverterState<T> {

	public abstract FunctionName methodToFunctionName(Namespace namespace, MethodName methodName);

	public abstract StructName classToStructName(Namespace namespace, ClassName className);

	protected OOToProceduralConverterState(
			StatementConverter<T> statementConverter,
			ExpressionConverter<T> expressionConverter,
			VariableReferenceConverter<T> variableReferenceConverter,
			TypeConverter typeConverter) {
		
		super(statementConverter, expressionConverter, variableReferenceConverter, typeConverter);
	}
}
