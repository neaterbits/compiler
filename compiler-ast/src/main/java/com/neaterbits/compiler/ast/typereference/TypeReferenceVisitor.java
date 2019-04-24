package com.neaterbits.compiler.ast.typereference;

public interface TypeReferenceVisitor<T, R> {

	R onBuiltinTypeReference(BuiltinTypeReference typeReference, T param);
	
	R onComplexTypeReference(ComplexTypeReference typeReference, T param);
	
	R onPointerTypeReference(PointerTypeReference typeReference, T param);
	
	R onFunctionPointerTypeReference(FunctionPointerTypeReference typeReference, T param);

	R onTypeDefTypeReference(TypeDefTypeReference typeReference, T param);
	
	R onUnnamedVoid(UnnamedVoidTypeReference typeReference, T param);
	
	R onResolveLaterTypeReference(ResolveLaterTypeReference typeReference, T param);
}
