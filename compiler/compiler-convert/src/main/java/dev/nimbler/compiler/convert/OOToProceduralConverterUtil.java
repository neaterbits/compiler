package dev.nimbler.compiler.convert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.block.Parameter;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.ParameterList;
import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerParameter;
import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;

public class OOToProceduralConverterUtil {

	public static ClassMethod findMethod(ClassType classType, MethodName methodName, ParameterList parameterList) {
		
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(parameterList);

		ClassMethod result = null;
		
		for (ComplexMemberDefinition member : classType.getMembers()) {
			
			if (member instanceof ClassMethodMember) {

				final ClassMethodMember classMethodMember = (ClassMethodMember)member;
				
				final ClassMethod classMethod = classMethodMember.getMethod();

				if (classMethod.getName().equals(methodName)) {
					
					boolean paramsMatch = true;
					
					if (classMethod.getParameters().size() == parameterList.getList().size()) {
						
						final Iterator<Parameter> classMethodParameterIter = classMethod.getParameters().iterator();
						final Iterator<Expression> paramListIter = parameterList.getList().iterator();

						while (classMethodParameterIter.hasNext()) {

							if (!classMethodParameterIter.next().getType().equals(paramListIter.next().getType())) {
								paramsMatch = false;
								break;
							}
						}
					}
					
					if (paramsMatch) {
						result = classMethod;
						break;
					}
				}
			}
		}
		
		
		return result;
	}
	
	public static FunctionPointerType makeFunctionPointerType(ClassMethod method, java.util.function.Function<BaseType, BaseType> convertMethodType) {
		
		final List<FunctionPointerParameter> parameters = new ArrayList<>(method.getParameters().size());
		
		method.getParameters().forEach(parameter -> {
			parameters.add(new FunctionPointerParameter(
					null, // convertMethodType.apply(parameter.getType()),
					parameter.getName()));
		});

		final FunctionPointerType functionPointerType = new FunctionPointerType(
				null, // convertMethodType.apply(method.getReturnType()),
				parameters); 
		
		return functionPointerType;
	}

}
