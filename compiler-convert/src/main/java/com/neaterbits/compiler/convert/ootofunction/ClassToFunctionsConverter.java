package com.neaterbits.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.ast.objects.CompilationCode;
import com.neaterbits.compiler.ast.objects.FieldNameDeclaration;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.block.ClassMethod;
import com.neaterbits.compiler.ast.objects.block.Function;
import com.neaterbits.compiler.ast.objects.block.FunctionName;
import com.neaterbits.compiler.ast.objects.block.FunctionQualifiers;
import com.neaterbits.compiler.ast.objects.block.MethodName;
import com.neaterbits.compiler.ast.objects.type.BaseType;
import com.neaterbits.compiler.ast.objects.type.CompleteName;
import com.neaterbits.compiler.ast.objects.type.FunctionPointerType;
import com.neaterbits.compiler.ast.objects.type.complex.ClassType;
import com.neaterbits.compiler.ast.objects.type.complex.StructType;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.StructDeclarationName;
import com.neaterbits.compiler.ast.objects.typedefinition.StructDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.StructName;
import com.neaterbits.compiler.ast.objects.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.FunctionPointerTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.convert.OOToProceduralConverterUtil;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMap;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;

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
			OOToProceduralDeclarations<?> alreadyConvertedMap,
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

					throw new UnsupportedOperationException();
					
					/*
					final ComplexTypeReference complexTypeReference = (ComplexTypeReference)fieldType;

					final TypeName type = complexTypeReference.getTypeName();
					
					if (type instanceof ClassType) {
						final StructType alreadyConverted = alreadyConvertedMap.getClassStructType(type);
						
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
					*/
				}
				else {
					throw new UnsupportedOperationException("Unknown field type " + fieldType);
				}
				
				final List<FieldNameDeclaration> fieldNameDeclarations = new ArrayList<>(field.getInitializers().size());
				
				for (InitializerVariableDeclarationElement initializer : field.getInitializers()) {
				    
				    final FieldNameDeclaration declaration = new FieldNameDeclaration(initializer.getNameDeclaration());
				    
				    fieldNameDeclarations.add(declaration);
				}
				
				final StructDataFieldMember structField = new StructDataFieldMember(
						field.getContext(),
						convertedTypeReference,
						fieldNameDeclarations);
				
				structMembers.add(structField);
			}
		}

		final StructName structName = classToStructName.apply(classType.getCompleteName());
		
		final StructDefinition struct = new StructDefinition(
				classDefinition.getContext(),
				new Keyword(classDefinition.getTypeKeyword().getContext(), "struct"),
				new StructDeclarationName(classDefinition.getName().getContext(), structName),
				structMembers);
		
		return new StructType(struct);
	}
	
	public static StructType convertClassMethodsToVTable(
			ClassType classType,
			OOToProceduralDeclarations<?> alreadyConvertedMap,
			ResolvedTypeCodeMap codeMap,
			java.util.function.Function<BaseType, BaseType> convertMethodType,
			java.util.function.Function<CompleteName, StructName> classToStructName,
			java.util.function.Function<TypeName, FieldNameDeclaration> classToFieldName,
			java.util.function.Function<MethodName, FieldNameDeclaration> methodToFieldName) {
		
		final ClassDefinition classDefinition = classType.getDefinition();
		
		final int numMembers = classDefinition.getMembers().size();
		final List<ComplexMemberDefinition> structMembers = new ArrayList<>(numMembers);

		final TypeInfo extendsFromTypeInfo = codeMap.getClassExtendsFromTypeInfo(classType.getCompleteName().toTypeName());
		
		if (extendsFromTypeInfo != null) {
			
			final UserDefinedTypeRef extendsFromType = codeMap.getType(extendsFromTypeInfo.getTypeNo());
			
			final StructType baseStructType = alreadyConvertedMap.getClassStructType(extendsFromType.getTypeName());
			
			if (baseStructType == null) {
				throw new IllegalStateException("No struct type for " + extendsFromType.getTypeName());
			}
			
			// Add a class member for the base type
			final StructDataFieldMember structDataFieldMember = new StructDataFieldMember(
					classDefinition.getContext(),
					new ComplexTypeReference(classDefinition.getContext(), baseStructType.getTypeName()),
					Arrays.asList(classToFieldName.apply(extendsFromType.getTypeName())));
			
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
						Arrays.asList(methodToFieldName.apply(methodMember.getMethod().getName())));
				
				structMembers.add(structMember);
			}
		}

		final StructName structName = classToStructName.apply(classType.getCompleteName());

		final StructDefinition struct = new StructDefinition(
				classDefinition.getContext(),
				new Keyword(classDefinition.getTypeKeyword().getContext(), "struct"),
				new StructDeclarationName(classDefinition.getName().getContext(), structName),
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
