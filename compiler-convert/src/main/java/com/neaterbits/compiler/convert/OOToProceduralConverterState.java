package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.FieldNameDeclaration;
import com.neaterbits.compiler.ast.block.FunctionName;
import com.neaterbits.compiler.ast.block.MethodName;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typedefinition.StructName;
import com.neaterbits.compiler.codemap.MethodInfo;
import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMap;
import com.neaterbits.compiler.util.TypeName;

public abstract class OOToProceduralConverterState<T extends OOToProceduralConverterState<T>>
			extends ConverterState<T> {

	public abstract FunctionName methodToFunctionName(CompleteName type, MethodName methodName);

	public abstract StructName classToStructName(CompleteName type);
	
	public abstract FieldNameDeclaration getVTableBaseFieldName(CompleteName type);
	
	public abstract FieldNameDeclaration getVTableFunctionFieldName(MethodName methodName);

	public abstract String getClassStaticMembersArrayName();
	
	public abstract String getClassStaticVTableArrayName();
	
	private final ResolvedTypeCodeMap<BuiltinType, ? extends BaseType, TypeName> codeMap;
	
	protected OOToProceduralConverterState(Converters<T> converters, ResolvedTypeCodeMap<BuiltinType, ? extends BaseType, TypeName> codeMap) {
		
		super(converters);
		
		this.codeMap = codeMap;
	}
	
	public int getTypeNo(TypeName type) {
		
		final TypeInfo typeInfo = codeMap.getTypeInfo(type);
		
		return typeInfo != null ? typeInfo.getTypeNo() : -1;
	}
	
	
	public MethodInfo getMethodInfo(TypeName classType, String methodName, TypeName [] parameterTypes) {
		final MethodInfo methodInfo = codeMap.getMethodInfo(classType, methodName, parameterTypes);

		return methodInfo;
	}
	
	public MethodDispatch getMethodDispatch(MethodInfo methodInfo) {
		
		final MethodDispatch methodDispatch;
		
		switch (methodInfo.getMethodVariant()) {
		case ABSTRACT:
			methodDispatch = MethodDispatch.VTABLE;
			break;
			
		case FINAL_IMPLEMENTATION:
			methodDispatch = MethodDispatch.NON_OVERRIDABLE;
			break;
			
		case OVERRIDABLE_IMPLEMENTATION:
			// Might optimize if few implementations
			methodDispatch = MethodDispatch.VTABLE;
			break;
			
		case STATIC:
		default:
			throw new UnsupportedOperationException();
		}
		
		return methodDispatch;
	}
	
}
