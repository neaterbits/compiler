package dev.nimbler.compiler.ast.objects.typereference;

public interface TypeReferenceVisitor<T, R> {

	R onScalarTypeReference(ScalarTypeReference typeReference, T param);

	R onComplexTypeReference(ComplexTypeReference typeReference, T param);
	
	R onPointerTypeReference(PointerTypeReference typeReference, T param);
	
	R onFunctionPointerTypeReference(FunctionPointerTypeReference typeReference, T param);

	R onTypeDefTypeReference(TypeDefTypeReference typeReference, T param);
	
	R onUnnamedVoid(UnnamedVoidTypeReference typeReference, T param);
	
	R onResolveLaterTypeReference(UnresolvedTypeReference typeReference, T param);
}
