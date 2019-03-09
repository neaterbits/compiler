package com.neaterbits.compiler.bytecode.common.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.bytecode.common.BaseClassFile;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassMethod;
import com.neaterbits.compiler.bytecode.common.MethodClassReferenceScanner;
import com.neaterbits.compiler.common.FieldType;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.codemap.MethodVariant;

public abstract class BuiltinClass extends BaseClassFile implements ClassBytecode {

	private final List<FieldType> fieldTypes;
	private final List<ClassMethod> methods;

	public BuiltinClass(List<ClassMethod> methods) {

		Objects.requireNonNull(methods);
		
		this.methods = methods;
		
		this.fieldTypes = new ArrayList<>();
	}
	
	final void addField(String name, FieldType fieldType) {
		fieldTypes.add(fieldType);
	}

	@Override
	public final int getFieldCount() {
		return fieldTypes.size();
	}

	@Override
	public final String getFieldName(int fieldIdx) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final FieldType getFieldType(int fieldIdx) {
		return fieldTypes.get(fieldIdx);
	}

	@Override
	public final boolean isStatic(int fieldIdx) {
		return false;
	}

	@Override
	public final TypeName getSuperClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getImplementedInterfacesCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TypeName getImplementedInterface(int interfaceIdx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final TypeVariant getTypeVariant() {
		return TypeVariant.CLASS;
	}

	@Override
	public final int getMethodCount() {
		return methods.size();
	}

	@Override
	public final String getMethodName(int methodIdx) {
		return methods.get(methodIdx).getName();
	}

	@Override
	public final FieldType getMethodSignature(int methodIdx, List<FieldType> parameterTypes) {
		
		final ClassMethod method = methods.get(methodIdx);
		
		parameterTypes.addAll(method.getParameterTypes());
		
		return method.getReturnType();
	}

	@Override
	public final MethodVariant getMethodVariant(int methodIdx) {
		return methods.get(methodIdx).getVariant();
	}

	@Override
	public final int getMethodIndex(String methodName, List<FieldType> parameterTypes) {

		int methodIdx = -1;
		
		for (int i = 0; i < getMethodCount(); ++ i) {
			final ClassMethod method = methods.get(i);
		
			if (method.getName().equals(methodName) && method.getParameterTypes().equals(parameterTypes)) {
				methodIdx = i;
				break;
			}
			
		}
		
		return methodIdx;
	}

	@Override
	public final FieldType getMethodReturnType(int methodIdx) {
		return methods.get(methodIdx).getReturnType();
	}

	@Override
	public final byte [] getMethodBytecode(int methodIdx) {
		return methods.get(methodIdx).getBytecode();
	}
	
	@Override
	public final int getMethodMaxOperandStack(int methodIdx) {
		return methods.get(methodIdx).getMaxOperandStack();
	}

	@Override
	public final int getMethodMaxLocals(int methodIdx) {
		return methods.get(methodIdx).getMaxLocals();
	}

	@Override
	public final <T> void scanMethodClassReferences(int methodIdx, MethodClassReferenceScanner<T> scanner, T param) {
		throw new UnsupportedOperationException();
	}
}
