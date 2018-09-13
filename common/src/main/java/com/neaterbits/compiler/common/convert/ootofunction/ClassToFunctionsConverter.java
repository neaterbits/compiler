package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.ResolvedTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.FunctionQualifiers;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;

/**
 * Converts a class to C code
 */


public class ClassToFunctionsConverter extends IterativeConverter {

	private final BaseTypeConverter<OOToProceduralConverterState> TYPE_CONVERSION_VISITOR = new BaseTypeConverter<OOToProceduralConverterState>() {
			
		@Override
		public TypeReference onString(StringType type, OOToProceduralConverterState param) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public TypeReference onPointer(PointerType type, OOToProceduralConverterState param) {
			
			final PointerType convertedType = new PointerType(
					convertType(type.getDelegate(), param),
					type.getLevels());
			
			return new ResolvedTypeReference(convertedType);
		}

		@Override
		public TypeReference onClass(ClassType type, OOToProceduralConverterState param) {
			
			
			return null;
		}

		@Override
		public TypeReference onStruct(StructType type, OOToProceduralConverterState param) {
			throw new UnsupportedOperationException();
		}
	};
	
	private TypeReference convertType(TypeReference toConvert, OOToProceduralConverterState state) {
		return convertType(toConvert.getType(), state);
	}

	private TypeReference convertType(BaseType toConvert, OOToProceduralConverterState state) {
		return toConvert.visit(TYPE_CONVERSION_VISITOR, state);
	}

	List<CompilationCode> convertClass(ClassDefinition classDefinition, OOToProceduralConverterState state) {

		final int numMembers = classDefinition.getMembers().size();
		final List<ComplexMemberDefinition> structMembers = new ArrayList<>(numMembers);
		final List<Function> functions = new ArrayList<>(numMembers);

		final Namespace namespace = state.getCurrentNamespace();

		for (ComplexMemberDefinition memberDefinition : classDefinition.getMembers()) {
			
			if (memberDefinition instanceof ClassDataFieldMember) {
				
				final ClassDataFieldMember field = (ClassDataFieldMember)memberDefinition;
				
				final StructDataFieldMember structField = new StructDataFieldMember(
						field.getContext(),
						state.convertTypeReference(field.getType()),
						field.getName());
				
				structMembers.add(structField);
			}
			else if (memberDefinition instanceof MethodMember) {
				
				// TODO dispatch tables
				
				final MethodMember methodMember = (MethodMember)memberDefinition;
				
				final Method method = methodMember.getMethod();
				
				final FunctionName functionName = state.methodToFunctionName(namespace, method.getName());
				
				final Function function = new Function(
						methodMember.getContext(),
						new FunctionQualifiers(false),
						convertType(method.getReturnType(), state),
						functionName,
						convertParameters(method.getParameters(), typeReference -> convertType(typeReference, state)),
						convertBlock(method.getBlock()));
				
				functions.add(function);
			}
			else {
				throw new UnsupportedOperationException("Unknown class member type " + memberDefinition.getClass());
			}
		}
		
		final StructDefinition struct = new StructDefinition(
				classDefinition.getContext(),
				state.classToStructName(namespace, (ClassName)classDefinition.getName()),
				structMembers);
		
		final List<CompilationCode> result = new ArrayList<>(1 + functions.size());
		
		result.add(struct);
		result.addAll(functions);
		
		return result;
	}
}
