package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.common.BuiltinTypeReference;
import com.neaterbits.compiler.common.ComplexTypeReference;
import com.neaterbits.compiler.common.FunctionPointerTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.FunctionQualifiers;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.FunctionPointerType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.FieldName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterUtil;
import com.neaterbits.compiler.common.resolver.CodeMap;
import com.neaterbits.compiler.common.resolver.codemap.TypeInfo;

/**
 * Converts a class to C code
 */

public class ClassToFunctionsConverter<T extends OOToProceduralConverterState<T>>
	extends IterativeConverter<T> {

	
	private TypeReference convertTypeReference(TypeReference toConvert, T state) {
		return state.convertTypeReference(toConvert);
	}

	public static StructType convertClassFieldsToStruct(
			ClassType classType,
			Map<ComplexType<?>, StructType> alreadyConvertedMap,
			List<ComplexTypeReference> convertLaterList,
			java.util.function.Function<TypeReference, TypeReference> convertFieldType,
			java.util.function.Function<CompleteName, StructName> classToStructName) {

		final ClassDefinition classDefinition = classType.getDefinition();
		
		final int numMembers = classDefinition.getMembers().size();
		final List<ComplexMemberDefinition> structMembers = new ArrayList<>(numMembers);

		for (ComplexMemberDefinition memberDefinition : classDefinition.getMembers()) {
		
			if (memberDefinition instanceof ClassDataFieldMember) {
				
				final ClassDataFieldMember field = (ClassDataFieldMember)memberDefinition;
				
				final TypeReference fieldType = field.getType();
				
				final TypeReference convertedTypeReference;
				
				if (fieldType instanceof BuiltinTypeReference) {
					 convertedTypeReference = convertFieldType.apply(fieldType);
					
				}
				else if (fieldType instanceof ComplexTypeReference) {
					
					final ComplexTypeReference complexTypeReference = (ComplexTypeReference)fieldType;

					final BaseType type = complexTypeReference.getType();
					
					if (type instanceof ClassType) {
						final StructType alreadyConverted = alreadyConvertedMap.get(type);
						
						if (alreadyConverted != null) {
							convertedTypeReference = new ComplexTypeReference(
									fieldType.getContext(),
									alreadyConverted);
						}
						else {
							// Must swap this later, when we have converted the reference class
							
							final ComplexTypeReference convertLaterReference = new ComplexTypeReference(
									fieldType.getContext(),
									(ClassType)type);
							
							convertedTypeReference = convertLaterReference;
							
							convertLaterList.add(convertLaterReference);
						}
					}
					else {
						throw new UnsupportedOperationException();
					}
				}
				else {
					throw new UnsupportedOperationException("Unknown field type " + fieldType);
				}
				
				
				final StructDataFieldMember structField = new StructDataFieldMember(
						field.getContext(),
						convertedTypeReference,
						field.getName());
				
				structMembers.add(structField);
			}
		}

		final StructName structName = classToStructName.apply(classType.getCompleteName());
		
		final StructDefinition struct = new StructDefinition(
				classDefinition.getContext(),
				structName,
				structMembers);
		
		return new StructType(struct);
	}
	
	public static StructType convertClassMethodsToVTable(
			ClassType classType,
			Map<ComplexType<?>, StructType> alreadyConvertedMap,
			CodeMap codeMap,
			java.util.function.Function<BaseType, BaseType> convertMethodType,
			java.util.function.Function<CompleteName, StructName> classToStructName,
			java.util.function.Function<CompleteName, FieldName> classToFieldName,
			java.util.function.Function<MethodName, FieldName> methodToFieldName) {
		

		final ClassDefinition classDefinition = classType.getDefinition();
		
		final int numMembers = classDefinition.getMembers().size();
		final List<ComplexMemberDefinition> structMembers = new ArrayList<>(numMembers);

		final TypeInfo extendsFromTypeInfo = codeMap.getClassExtendsFromTypeInfo(classType.getCompleteName());
		
		if (extendsFromTypeInfo != null) {
			
			final ComplexType<?> extendsFromType = codeMap.getType(extendsFromTypeInfo.getTypeNo());
			
			final StructType baseStructType = alreadyConvertedMap.get(extendsFromType);
			
			if (baseStructType == null) {
				throw new IllegalStateException("No struct type for " + extendsFromType.getCompleteName());
			}
			
			// Add a class member for the base type
			final StructDataFieldMember structDataFieldMember = new StructDataFieldMember(
					classDefinition.getContext(),
					new ComplexTypeReference(classDefinition.getContext(), baseStructType),
					classToFieldName.apply(extendsFromType.getCompleteName()));
			
			structMembers.add(structDataFieldMember);
		}
		
		for (ComplexMemberDefinition memberDefinition : classDefinition.getMembers()) {
			
			if (memberDefinition instanceof ClassMethodMember) {
				
				final ClassMethodMember methodMember = (ClassMethodMember)memberDefinition;
				
				final ClassMethod method = methodMember.getMethod();
				
				final FunctionPointerType functionPointerType = OOToProceduralConverterUtil.makeFunctionPointerType(method, convertMethodType);
				
				final StructDataFieldMember structMember = new StructDataFieldMember(
						methodMember.getContext(),
						new FunctionPointerTypeReference(methodMember.getContext(), functionPointerType),
						methodToFieldName.apply(methodMember.getMethod().getName()));
				
				structMembers.add(structMember);
			}
		}

		final StructName structName = classToStructName.apply(classType.getCompleteName());

		final StructDefinition struct = new StructDefinition(
				classDefinition.getContext(),
				structName,
				structMembers);
		
		return new StructType(struct);
	}
	
	List<CompilationCode> convertClass(CompleteName completeTypeName, ClassDefinition classDefinition, T state) {
		

		final int numMembers = classDefinition.getMembers().size();
		final List<Function> functions = new ArrayList<>(numMembers);

		System.out.println("### convert class " + classDefinition.getName());
		
		for (ComplexMemberDefinition memberDefinition : classDefinition.getMembers()) {
			
			if (memberDefinition instanceof ClassMethodMember) {
				
				// TODO dispatch tables
				
				final ClassMethodMember methodMember = (ClassMethodMember)memberDefinition;
				
				final ClassMethod method = methodMember.getMethod();
				
				final FunctionName functionName = state.methodToFunctionName(completeTypeName, method.getName());
				
				final Function function = new Function(
						methodMember.getContext(),
						new FunctionQualifiers(false),
						convertTypeReference(method.getReturnType(), state),
						functionName,
						convertParameters(method.getParameters(), typeReference -> convertTypeReference(typeReference, state)),
						convertBlock(method.getBlock(), state));
				
				functions.add(function);
			}
			else if (memberDefinition instanceof ClassDataFieldMember) {
				// Already converted
			}
			else {
				throw new UnsupportedOperationException("Unknown class member type " + memberDefinition.getClass());
			}
		}
		
		final List<CompilationCode> result = new ArrayList<>(1 + functions.size());
		
		result.addAll(functions);
		
		System.out.println("## return functions " + result);
		
		return result;
	}
}
