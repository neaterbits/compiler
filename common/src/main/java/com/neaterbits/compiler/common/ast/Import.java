package com.neaterbits.compiler.common.ast;

import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.common.util.Strings;

public final class Import extends BaseASTElement {

	enum Type {
		KNOWN_NAMESPACE,
		NAMESPACE_OR_TYPE,
		KNOWN_METHOD,
		WILDCARD_METHOD
	}
	
	private final NamespaceReference namespace;
	private final ClassOrInterfaceName typeName;
	private final String [] namespaceOrTypeName;
	private final Type type;
	private final MethodName method;

	public Import(Context context, NamespaceReference namespace, ClassOrInterfaceName typeName) {
		super(context);
		
		this.namespace = namespace;
		this.typeName = typeName;
		this.namespaceOrTypeName = null;
		this.type = Type.KNOWN_NAMESPACE;
		this.method = null;
	}
	
	public Import(Context context, String[] namespaceOrTypeName) {
		super(context);
		this.namespace = null;
		this.typeName = null;
		this.namespaceOrTypeName = namespaceOrTypeName;
		this.type = Type.NAMESPACE_OR_TYPE;
		this.method = null;
	}

	public Import(Context context, NamespaceReference namespace, ClassOrInterfaceName typeName, MethodName method) {
		super(context);
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(typeName);
		
		this.namespace = namespace;
		this.typeName = typeName;
		this.namespaceOrTypeName = null;
		this.type = method != null ? Type.KNOWN_METHOD : Type.WILDCARD_METHOD;
		this.method = method;
	}

	public NamespaceReference getNamespace() {
		return namespace;
	}

	public ClassOrInterfaceName getTypeName() {
		return typeName;
	}

	public String[] getNamespaceOrTypeName() {
		return namespaceOrTypeName;
	}

	public boolean isMethodImport() {
		return type == Type.KNOWN_METHOD || type == Type.WILDCARD_METHOD;
	}

	public MethodName getMethod() {
		return method;
	}

	public boolean startsWith(String [] parts) {
	
		final boolean startsWith;
		
		switch (type) {
		case KNOWN_NAMESPACE:
		case KNOWN_METHOD:
		case WILDCARD_METHOD:
			startsWith = namespace.startsWith(parts);
			break;
			
		case NAMESPACE_OR_TYPE:
			startsWith = Strings.startsWith(namespaceOrTypeName, parts);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown type " + type);
		}
		
		return startsWith;
	}
	
	public Import removeFromNamespace(String [] parts) {
		
		final Import result;
		
		switch (type) {
		case KNOWN_NAMESPACE:
			result = new Import(getContext(), namespace.removeFromNamespace(parts), typeName);
			break;
			
		case KNOWN_METHOD:
		case WILDCARD_METHOD:
			result = new Import(getContext(), namespace.removeFromNamespace(parts), typeName, method);
			break;
			
		case NAMESPACE_OR_TYPE:
			
			if (!startsWith(parts)) {
				throw new IllegalArgumentException("Does not start with parts " + Arrays.toString(parts));
			}
			
			result = new Import(getContext(), Strings.lastOf(this.namespaceOrTypeName, this.namespaceOrTypeName.length - parts.length));
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown type " + type);
		}
		
		return result;
	}
	
	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}

	@Override
	public String toString() {
		return "Import [namespace=" + namespace + ", className=" + typeName + ", packageOrTypeName="
				+ Arrays.toString(namespaceOrTypeName) + ", type=" + type + ", method=" + method + "]";
	}
}
