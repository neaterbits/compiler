package com.neaterbits.compiler.types;

public enum Mutability {
	MUTABLE,
	VALUE_OR_REF_IMMUTABLE,				// final in java
	VALUE_OR_OBJECT_IMMUTABLE,			// const in C++ ?
	VALUE_OR_OBJECT_OR_REF_IMMUTABLE;	// const & const in C++ ?
	
}
