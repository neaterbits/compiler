package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.primitive.IntType;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;
import com.neaterbits.compiler.common.convert.Converters;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.common.resolver.CodeMap;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.java.JavaTypes;

public class MappingJavaToCConverterState<T extends MappingJavaToCConverterState<T>> extends OOToProceduralConverterState<T> {

	public MappingJavaToCConverterState(Converters<T> converters, CodeMap codeMap) {
		super(converters, codeMap);
	}

	private static String className(CompleteName type) {
		final StringBuilder sb = new StringBuilder();

		className(type, sb);
		
		return sb.toString();
	}

	private static void className(CompleteName type, StringBuilder sb) {
		
		sb.append(Strings.join(type.getNamespace().getParts(), '_'));
		
		if (type.getOuterTypes() != null) {
			for (TypeName typeName : type.getOuterTypes()) {
				sb.append('_').append(typeName.getName());
			}
		}
	}
	
	@Override
	public final FunctionName methodToFunctionName(CompleteName type, MethodName methodName) {

		final StringBuilder sb = new StringBuilder();
		
		className(type, sb);
		
		sb.append('_').append(methodName.getName());
		
		return new FunctionName(sb.toString());
	}

	@Override
	public final StructName classToStructName(CompleteName completeName) {
		return new StructName(className(completeName));
	}

	@Override
	public final String getClassStaticMembersArrayName() {
		return "class_static_members";
	}

	@Override
	public final IntType getIntType() {
		return JavaTypes.INT_TYPE;
	}
}
