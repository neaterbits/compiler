package com.neaterbits.compiler.ast;

enum ImportType {
	KNOWN_NAMESPACE,
	NAMESPACE_OR_TYPE,
	KNOWN_METHOD,
	WILDCARD_METHOD
}
