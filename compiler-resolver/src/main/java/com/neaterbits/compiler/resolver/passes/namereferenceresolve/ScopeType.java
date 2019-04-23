package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

enum ScopeType {
	COMPILATION_UNIT,
	NAMESPACE,
	COMPLEX_TYPE,
	METHOD,
	BLOCK;
}