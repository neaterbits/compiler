package com.neaterbits.compiler.ast;

import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.compiler.ast.block.MethodName;
import com.neaterbits.compiler.ast.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

public final class ImportPackage extends BaseASTElement {

	private final NamespaceReference namespace;
	private final ClassOrInterfaceName typeName;
	private final String [] namespaceOrTypeName;
	private final ImportType type;
	private final MethodName method;

	public ImportPackage(Context context, NamespaceReference namespace, ClassOrInterfaceName typeName) {
		super(context);
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(typeName);
		
		this.namespace = namespace;
		this.typeName = typeName;
		this.namespaceOrTypeName = null;
		this.type = ImportType.KNOWN_NAMESPACE;
		this.method = null;
	}
	
	public ImportPackage(Context context, String[] namespaceOrTypeName) {
		super(context);
		
		Objects.requireNonNull(namespaceOrTypeName);
		
		this.namespace = null;
		this.typeName = null;
		this.namespaceOrTypeName = namespaceOrTypeName;
		this.type = ImportType.NAMESPACE_OR_TYPE;
		this.method = null;
	}

	public ImportPackage(Context context, NamespaceReference namespace, ClassOrInterfaceName typeName, MethodName method) {
		super(context);
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(typeName);
		
		this.namespace = namespace;
		this.typeName = typeName;
		this.namespaceOrTypeName = null;
		this.type = method != null ? ImportType.KNOWN_METHOD : ImportType.WILDCARD_METHOD;
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
		return type == ImportType.KNOWN_METHOD || type == ImportType.WILDCARD_METHOD;
	}

	public MethodName getMethod() {
		return method;
	}
	
	public void visit(TypeImportVisitor visitor) {
		
		
		switch (type) {
		case KNOWN_NAMESPACE:
			visitor.onKnownNamespace(namespace.getParts(), typeName.getName());
			break;
			
		case NAMESPACE_OR_TYPE:
			visitor.onNamespaceOrTypeName(namespaceOrTypeName);
			break;
			
		case KNOWN_METHOD:
			visitor.onKnownStaticMethod(namespace.getParts(), typeName.getName(), method.getName());
			break;
			
		case WILDCARD_METHOD:
			visitor.onStaticMethodWildcard(namespace.getParts(), typeName.getName());
			break;
		}
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
	
	public ImportPackage removeFromNamespace(String [] parts) {
		
		final ImportPackage result;
		
		switch (type) {
		case KNOWN_NAMESPACE:
			result = new ImportPackage(getContext(), namespace.removeFromNamespace(parts), typeName);
			break;
			
		case KNOWN_METHOD:
		case WILDCARD_METHOD:
			result = new ImportPackage(getContext(), namespace.removeFromNamespace(parts), typeName, method);
			break;
			
		case NAMESPACE_OR_TYPE:
			
			if (!startsWith(parts)) {
				throw new IllegalArgumentException("Does not start with parts " + Arrays.toString(parts));
			}
			
			result = new ImportPackage(getContext(), Strings.lastOf(this.namespaceOrTypeName, this.namespaceOrTypeName.length - parts.length));
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
