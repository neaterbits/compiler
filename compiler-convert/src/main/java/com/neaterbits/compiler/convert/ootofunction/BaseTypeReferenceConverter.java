package com.neaterbits.compiler.convert.ootofunction;

import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.FunctionPointerType;
import com.neaterbits.compiler.ast.type.PointerType;
import com.neaterbits.compiler.ast.type.TypeDefType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.FunctionPointerTypeReference;
import com.neaterbits.compiler.ast.typereference.PointerTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeDefTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.convert.ConverterState;
import com.neaterbits.compiler.convert.TypeReferenceConverter;

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
				(ComplexType<?, ?, ?>)convertType(typeReference.getType(), param));
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
