package com.neaterbits.compiler.common.convert;



import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.typedefinition.FieldName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;
import com.neaterbits.compiler.common.resolver.ResolvedTypeCodeMap;
import com.neaterbits.compiler.common.resolver.codemap.MethodInfo;
import com.neaterbits.compiler.common.resolver.codemap.TypeInfo;

public abstract class OOToProceduralConverterState<T extends OOToProceduralConverterState<T>>
			extends ConverterState<T> {

	public abstract FunctionName methodToFunctionName(CompleteName type, MethodName methodName);

	public abstract StructName classToStructName(CompleteName type);
	
	public abstract FieldName getVTableBaseFieldName(CompleteName type);
	
	public abstract FieldName getVTableFunctionFieldName(MethodName methodName);

	public abstract String getClassStaticMembersArrayName();
	
	public abstract String getClassStaticVTableArrayName();
	
	private final ResolvedTypeCodeMap codeMap;
	
	protected OOToProceduralConverterState(Converters<T> converters, ResolvedTypeCodeMap codeMap) {
		
		super(converters);
		
		this.codeMap = codeMap;
	}
	
	public int getTypeNo(BaseType type) {
		
		final TypeInfo typeInfo = codeMap.getTypeInfo(type);
		
		return typeInfo != null ? typeInfo.getTypeNo() : -1;
	}
	
	
	public MethodInfo getMethodInfo(ClassType classType, MethodName methodName, NamedType [] parameterTypes) {
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
