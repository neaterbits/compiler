package dev.nimbler.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.FieldNameDeclaration;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Function;
import dev.nimbler.compiler.ast.objects.block.FunctionName;
import dev.nimbler.compiler.ast.objects.block.FunctionQualifiers;
import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.StructType;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.StructName;
import dev.nimbler.compiler.ast.objects.typereference.BuiltinTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.ComplexTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.FunctionPointerTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.convert.OOToProceduralConverterState;
import dev.nimbler.compiler.convert.OOToProceduralConverterUtil;
import dev.nimbler.language.codemap.TypeInfo;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

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
			CompilerCodeMap codeMap,
			java.util.function.Function<BaseType, BaseType> convertMethodType,
			java.util.function.Function<CompleteName, StructName> classToStructName,
			java.util.function.Function<TypeName, FieldNameDeclaration> classToFieldName,
			java.util.function.Function<MethodName, FieldNameDeclaration> methodToFieldName) {

		final ClassDefinition classDefinition = classType.getDefinition();

		final int numMembers = classDefinition.getMembers().size();
		final List<ComplexMemberDefinition> structMembers = new ArrayList<>(numMembers);

		final TypeInfo extendsFromTypeInfo = codeMap.getClassExtendsFromTypeInfo(classType.getCompleteName().toTypeName());

		if (extendsFromTypeInfo != null) {

			final TypeName extendsFromType = codeMap.getTypeName(extendsFromTypeInfo.getTypeNo());

			final StructType baseStructType = alreadyConvertedMap.getClassStructType(extendsFromType);

			if (baseStructType == null) {
				throw new IllegalStateException("No struct type for " + extendsFromType);
			}

			// Add a class member for the base type
			final StructDataFieldMember structDataFieldMember = new StructDataFieldMember(
					classDefinition.getContext(),
					new ComplexTypeReference(
					        classDefinition.getContext(),
					        -1,
					        baseStructType.getTypeName()),
					Arrays.asList(classToFieldName.apply(extendsFromType)));

			structMembers.add(structDataFieldMember);
		}

		for (ComplexMemberDefinition memberDefinition : classDefinition.getMembers()) {

			if (memberDefinition instanceof ClassMethodMember) {

				final ClassMethodMember methodMember = (ClassMethodMember)memberDefinition;

				final ClassMethod method = methodMember.getMethod();

				final FunctionPointerType functionPointerType = OOToProceduralConverterUtil.makeFunctionPointerType(method, convertMethodType);

				final StructDataFieldMember structMember = new StructDataFieldMember(
						methodMember.getContext(),
						new FunctionPointerTypeReference(methodMember.getContext(), -1, functionPointerType),
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
						null,
						new FunctionQualifiers(false),
						convertTypeReference(method.getReturnType(), state),
						functionName,
						convertParameters(method.getParameters(), typeReference -> convertTypeReference(typeReference, state)),
						null,
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
