package dev.nimbler.compiler.convert.ootofunction;

import dev.nimbler.compiler.ast.objects.type.FunctionPointerType;
import dev.nimbler.compiler.ast.objects.type.PointerType;
import dev.nimbler.compiler.ast.objects.typereference.ComplexTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.FunctionPointerTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.PointerTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.ScalarTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeDefTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.typereference.UnnamedVoidTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.UnresolvedTypeReference;
import dev.nimbler.compiler.convert.ConverterState;
import dev.nimbler.compiler.convert.TypeReferenceConverter;

public abstract class BaseTypeReferenceConverter<T extends ConverterState<T>>
		extends BaseConverter<T>
		implements TypeReferenceConverter<T> {

	@Override
	public TypeReference onScalarTypeReference(ScalarTypeReference typeReference, T param) {
		return new ScalarTypeReference(
				typeReference.getContext(),
				typeReference.getTypeNo(),
				convertType(typeReference, param).getTypeName());
	}

	@Override
	public TypeReference onComplexTypeReference(ComplexTypeReference typeReference, T param) {
		return new ComplexTypeReference(
		        typeReference.getContext(),
		        typeReference.getTypeNo(),
		        typeReference.getTypeName());
	}

	@Override
	public TypeReference onPointerTypeReference(PointerTypeReference typeReference, T param) {
		return new PointerTypeReference(
				typeReference.getContext(),
				typeReference.getTypeNo(),
				(PointerType)convertType(typeReference.getType(), param));
	}

	@Override
	public TypeReference onFunctionPointerTypeReference(FunctionPointerTypeReference typeReference, T param) {
		return new FunctionPointerTypeReference(
				typeReference.getContext(),
				typeReference.getTypeNo(),
				(FunctionPointerType)convertType(typeReference.getType(), param));
	}

	@Override
	public TypeReference onTypeDefTypeReference(TypeDefTypeReference typeReference, T param) {
		return new TypeDefTypeReference(
				typeReference.getContext(),
				typeReference.getTypeNo(),
				typeReference.getTypeName(),
				typeReference.getAliasedType());
	}

	@Override
	public TypeReference onResolveLaterTypeReference(UnresolvedTypeReference typeReference, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeReference onUnnamedVoid(UnnamedVoidTypeReference typeReference, T param) {
		throw new UnsupportedOperationException();
	}
}
