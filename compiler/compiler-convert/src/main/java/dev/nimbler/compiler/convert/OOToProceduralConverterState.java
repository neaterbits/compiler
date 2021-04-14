package dev.nimbler.compiler.convert;

import dev.nimbler.compiler.ast.objects.FieldNameDeclaration;
import dev.nimbler.compiler.ast.objects.block.FunctionName;
import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.typedefinition.StructName;
import dev.nimbler.language.codemap.MethodInfo;
import dev.nimbler.language.codemap.TypeInfo;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

public abstract class OOToProceduralConverterState<T extends OOToProceduralConverterState<T>>
			extends ConverterState<T> {

	public abstract FunctionName methodToFunctionName(CompleteName type, MethodName methodName);

	public abstract StructName classToStructName(CompleteName type);

	public abstract FieldNameDeclaration getVTableBaseFieldName(CompleteName type);

	public abstract FieldNameDeclaration getVTableFunctionFieldName(MethodName methodName);

	public abstract String getClassStaticMembersArrayName();

	public abstract String getClassStaticVTableArrayName();

	private final CompilerCodeMap codeMap;

	protected OOToProceduralConverterState(Converters<T> converters, CompilerCodeMap codeMap) {

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
