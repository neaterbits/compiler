package com.neaterbits.compiler.convert.ootofunction;

import com.neaterbits.compiler.ast.type.FunctionPointerType;
import com.neaterbits.compiler.ast.type.PointerType;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.FunctionPointerTypeReference;
import com.neaterbits.compiler.ast.typereference.PointerTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeDefTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.typereference.UnnamedVoidTypeReference;
import com.neaterbits.compiler.convert.ConverterState;
import com.neaterbits.compiler.convert.TypeReferenceConverter;

public abstract class BaseTypeReferenceConverter<T extends ConverterState<T>>
		extends BaseConverter<T>
		implements TypeReferenceConverter<T> {

	@Override
	public TypeReference onBuiltinTypeReference(BuiltinTypeReference typeReference, T param) {
		return new BuiltinTypeReference(
				typeReference.getContext(),
				convertType(typeReference, param).getTypeName(),
				typeReference.isScalar());
	}

	@Override
	public TypeReference onComplexTypeReference(ComplexTypeReference typeReference, T param) {
		return new ComplexTypeReference(typeReference.getContext(), typeReference.getTypeName());
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
				typeReference.getTypeName(),
				typeReference.getAliasedType());
	}

	@Override
	public TypeReference onResolveLaterTypeReference(ResolveLaterTypeReference typeReference, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeReference onUnnamedVoid(UnnamedVoidTypeReference typeReference, T param) {
		throw new UnsupportedOperationException();
	}
}
