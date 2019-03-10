package com.neaterbits.compiler.convert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.MethodName;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.ParameterList;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.FunctionPointerParameter;
import com.neaterbits.compiler.ast.type.FunctionPointerType;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;

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

							if (!classMethodParameterIter.next().getType().getType().equals(paramListIter.next().getType())) {
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
					convertMethodType.apply(parameter.getType().getType()),
					parameter.getName()));
		});

		final FunctionPointerType functionPointerType = new FunctionPointerType(
				convertMethodType.apply(method.getReturnType().getType()),
				parameters); 
		
		return functionPointerType;
	}

}
