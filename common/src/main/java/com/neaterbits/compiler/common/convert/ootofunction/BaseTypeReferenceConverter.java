package com.neaterbits.compiler.common.convert.ootofunction;

import com.neaterbits.compiler.common.BuiltinTypeReference;
import com.neaterbits.compiler.common.ComplexTypeReference;
import com.neaterbits.compiler.common.FunctionPointerTypeReference;
import com.neaterbits.compiler.common.PointerTypeReference;
import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.TypeDefTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.FunctionPointerType;
import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.TypeDefType;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.common.convert.ConverterState;
import com.neaterbits.compiler.common.convert.TypeReferenceConverter;

public abstract class BaseTypeReferenceConverter<T extends ConverterState<T>> implements TypeReferenceConverter<T> {

	protected final BaseType convertType(BaseType type, T param) {
		return param.convertType(type);
	}
	
	@Override
	public TypeReference onBuiltinTypeReference(BuiltinTypeReference typeReference, T param) {
		return new BuiltinTypeReference(
				typeReference.getContext(),
				(BuiltinType)convertType(typeReference.getType(), param));
	}

	@Override
	public TypeReference onComplexTypeReference(ComplexTypeReference typeReference, T param) {
		return new ComplexTypeReference(
				typeReference.getContext(),
				(ComplexType<?>)convertType(typeReference.getType(), param));
	}

	@Override
	public TypeReference onPointerTypeReference(PointerTypeReference typeReference, T param) {
		return new PointerTypeReference(
				typeReference.getContext(),
				(PointerType)convertType(typeReference.getType(), param));
	}
	
	@Override
	public TypeReference onFunctionPointerTypeReference(FunctionPointerTypeReference typeReference, T param) {
		return new FunctionPointerTypeReference(
				typeReference.getContext(),
				(FunctionPointerType)convertType(typeReference.getType(), param));
	}

	@Override
	public TypeReference onTypeDefTypeReference(TypeDefTypeReference typeReference, T param) {
		return new TypeDefTypeReference(
				typeReference.getContext(),
				(TypeDefType)convertType(typeReference.getType(), param));
	}

	@Override
	public TypeReference onResolveLaterTypeReference(ResolveLaterTypeReference typeReference, T param) {
		throw new UnsupportedOperationException();
	}
}
