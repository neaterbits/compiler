package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.PointerTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.FunctionQualifiers;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;

/**
 * Converts a class to C code
 */


public class ClassToFunctionsConverter<T extends OOToProceduralConverterState<T>>
	extends IterativeConverter<T> {

	
	private final BaseTypeConverter TYPE_CONVERSION_VISITOR = new BaseTypeConverter() {
			
		@Override
		public TypeReference onString(StringType type, Context param) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public TypeReference onPointer(PointerType type, Context param) {
			
			final PointerType convertedType = new PointerType(
					convertType(type.getDelegate()),
					type.getLevels());
			
			return new PointerTypeReference(param, convertedType);
		}

		@Override
		public TypeReference onClass(ClassType type, Context param) {
			return null;
		}

		@Override
		public TypeReference onStruct(StructType type, Context param) {
			throw new UnsupportedOperationException();
		}
	};
	
	private TypeReference convertType(TypeReference toConvert) {
		return convertType(toConvert.getType(), toConvert.getContext());
	}

	private TypeReference convertType(BaseType toConvert, Context state) {
		return toConvert.visit(TYPE_CONVERSION_VISITOR, state);
	}

	List<CompilationCode> convertClass(ClassDefinition classDefinition, T state) {

		final int numMembers = classDefinition.getMembers().size();
		final List<ComplexMemberDefinition> structMembers = new ArrayList<>(numMembers);
		final List<Function> functions = new ArrayList<>(numMembers);

		System.out.println("### convert class " + classDefinition.getName());
		
		final Namespace namespace = state.getCurrentNamespace();

		for (ComplexMemberDefinition memberDefinition : classDefinition.getMembers()) {
			
			if (memberDefinition instanceof ClassDataFieldMember) {
				
				final ClassDataFieldMember field = (ClassDataFieldMember)memberDefinition;
				
				final StructDataFieldMember structField = new StructDataFieldMember(
						field.getContext(),
						state.convertTypeReference(field.getType()),
						field.getName());
				
				structMembers.add(structField);
				
				System.out.println("## add struct field " + structField.getName());
			}
			else if (memberDefinition instanceof ClassMethodMember) {
				
				// TODO dispatch tables
				
				final ClassMethodMember methodMember = (ClassMethodMember)memberDefinition;
				
				final ClassMethod method = methodMember.getMethod();
				
				final FunctionName functionName = state.methodToFunctionName(namespace, method.getName());
				
				final Function function = new Function(
						methodMember.getContext(),
						new FunctionQualifiers(false),
						convertType(method.getReturnType()),
						functionName,
						convertParameters(method.getParameters(), typeReference -> convertType(typeReference)),
						convertBlock(method.getBlock(), state));
				
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
		
		System.out.println("## return functions " + result);
		
		return result;
	}
}
