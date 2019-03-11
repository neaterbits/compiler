package com.neaterbits.compiler.main;

import com.neaterbits.compiler.ast.block.FunctionName;
import com.neaterbits.compiler.ast.block.MethodName;
import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.type.primitive.IntType;
import com.neaterbits.compiler.ast.typedefinition.FieldName;
import com.neaterbits.compiler.ast.typedefinition.StructName;
import com.neaterbits.compiler.convert.Converters;
import com.neaterbits.compiler.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.java.JavaTypes;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMap;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;

public class MappingJavaToCConverterState<T extends MappingJavaToCConverterState<T>> extends OOToProceduralConverterState<T> {

	public MappingJavaToCConverterState(
			Converters<T> converters,
			ResolvedTypeCodeMap<BuiltinType, ComplexType<?, ?, ?>, TypeName> codeMap) {
		
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
			for (BaseTypeName typeName : type.getOuterTypes()) {
				sb.append('_').append(typeName.getName());
			}
		}
		
		if (sb.length() > 0) {
			sb.append('_');
		}
		
		sb.append(type.getName().getName());
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
	public final FieldName getVTableBaseFieldName(CompleteName type) {
		return new FieldName("base_" + className(type));
	}

	@Override
	public final FieldName getVTableFunctionFieldName(MethodName methodName) {
		return new FieldName(methodName.getName());
	}

	@Override
	public final String getClassStaticMembersArrayName() {
		return "class_static_members";
	}

	@Override
	public String getClassStaticVTableArrayName() {
		return "class_vtables";
	}

	@Override
	public final IntType getIntType() {
		return JavaTypes.INT_TYPE;
	}
}
