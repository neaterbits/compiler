package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.common.util.Strings;

final class JavaToCConverterState extends OOToProceduralConverterState<JavaToCConverterState> {
	
	public JavaToCConverterState() {
		super(
				new JavaToCStatementConverter(),
				new JavaToCExpressionConverter(),
				new JavaToCVariableReferenceConverter(),
				new JavaToCTypeConverter());
	}

	@Override
	public FunctionName methodToFunctionName(Namespace namespace, MethodName methodName) {
		return new FunctionName(Strings.join(namespace.getParts(), '_') + '_' + methodName.getName());
	}

	@Override
	public StructName classToStructName(Namespace namespace, ClassName className) {
		return new StructName(Strings.join(namespace.getParts(), '_') + '_' + className.getName());
	}
}
