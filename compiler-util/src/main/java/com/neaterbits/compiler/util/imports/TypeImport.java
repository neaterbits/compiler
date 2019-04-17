package com.neaterbits.compiler.util.imports;

import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

public class TypeImport {
	
	private final String [] namespace;
	private final String typeName;
	private final String [] namespaceOrTypeName;
	private final ImportType type;
	private final String method;

	public TypeImport(String [] namespace, String typeName) {
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(typeName);
		
		this.namespace = namespace;
		this.typeName = typeName;
		this.namespaceOrTypeName = null;
		this.type = ImportType.KNOWN_TYPE;
		this.method = null;
	}
	
	public TypeImport(String[] namespaceOrTypeName) {

		Objects.requireNonNull(namespaceOrTypeName);
		
		this.namespace = null;
		this.typeName = null;
		this.namespaceOrTypeName = namespaceOrTypeName;
		this.type = ImportType.ON_DEMAND_NAMESPACE_OR_TYPE;
		this.method = null;
	}

	public TypeImport(String [] namespace, String typeName, String method) {
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(typeName);
		
		this.namespace = namespace;
		this.typeName = typeName;
		this.namespaceOrTypeName = null;
		this.type = method != null ? ImportType.STATIC_KNOWN_METHOD : ImportType.STATIC_ON_DEMAND_METHOD;
		this.method = method;
	}

	public String [] getNamespace() {
		return namespace;
	}

	public String getTypeName() {
		return typeName;
	}

	public String[] getNamespaceOrTypeName() {
		return namespaceOrTypeName;
	}

	public boolean isMethodImport() {
		return type == ImportType.STATIC_KNOWN_METHOD || type == ImportType.STATIC_ON_DEMAND_METHOD;
	}

	public String getMethod() {
		return method;
	}
	
	public void visit(TypeImportVisitor visitor) {
		
		switch (type) {
		case KNOWN_TYPE:
			visitor.onKnownNamespace(namespace, typeName);
			break;
			
		case ON_DEMAND_NAMESPACE_OR_TYPE:
			visitor.onNamespaceOrTypeName(namespaceOrTypeName);
			break;
			
		case STATIC_KNOWN_METHOD:
			visitor.onKnownStaticMethod(namespace, typeName, method);
			break;
			
		case STATIC_ON_DEMAND_METHOD:
			visitor.onStaticMethodWildcard(namespace, typeName);
			break;
		}
	}
	
	public boolean startsWith(String [] parts) {
	
		final boolean startsWith;
		
		switch (type) {
		case KNOWN_TYPE:
		case STATIC_KNOWN_METHOD:
		case STATIC_ON_DEMAND_METHOD:
			startsWith = Strings.startsWith(this.namespace, parts);
			break;
			
		case ON_DEMAND_NAMESPACE_OR_TYPE:
			startsWith = Strings.startsWith(namespaceOrTypeName, parts);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown type " + type);
		}
		
		return startsWith;
	}
	
	private String [] removePartsFromNamespace(String [] parts) {
		
		if (!startsWith(parts)) {
			throw new IllegalArgumentException("Does not start with parts " + Arrays.toString(parts));
		}

		return Strings.lastOf(this.namespace, this.namespace.length - parts.length);
	}

	public TypeImport removeFromNamespace(String [] parts) {
		
		final TypeImport result;
		
		switch (type) {
		case KNOWN_TYPE:
			result = new TypeImport(removePartsFromNamespace(parts), typeName);
			break;
			
		case STATIC_KNOWN_METHOD:
		case STATIC_ON_DEMAND_METHOD:
			result = new TypeImport(removePartsFromNamespace(parts), typeName, method);
			break;
			
		case ON_DEMAND_NAMESPACE_OR_TYPE:
			
			if (!startsWith(parts)) {
				throw new IllegalArgumentException("Does not start with parts " + Arrays.toString(parts));
			}
			
			result = new TypeImport(Strings.lastOf(this.namespaceOrTypeName, this.namespaceOrTypeName.length - parts.length));
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown type " + type);
		}
		
		return result;
	}


	@Override
	public String toString() {
		return "Import [namespace=" + namespace + ", className=" + typeName + ", packageOrTypeName="
				+ Arrays.toString(namespaceOrTypeName) + ", type=" + type + ", method=" + method + "]";
	}

}
