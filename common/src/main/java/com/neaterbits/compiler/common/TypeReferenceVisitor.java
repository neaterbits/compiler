package com.neaterbits.compiler.common;

public interface TypeReferenceVisitor<T, R> {

	R onBuiltinTypeReference(BuiltinTypeReference typeReference, T param);
	
	R onComplexTypeReference(ComplexTypeReference typeReference, T param);
	
	R onPointerTypeReference(PointerTypeReference typeReference, T param);
	
	R onResolveLaterTypeReference(ResolveLaterTypeReference typeReference, T param);
}
