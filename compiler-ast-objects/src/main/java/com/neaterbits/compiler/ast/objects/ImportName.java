package com.neaterbits.compiler.ast.objects;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.block.MethodName;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.TypeImportVisitor;
import com.neaterbits.compiler.util.name.NamespaceReference;

public final class ImportName extends BaseASTElement {

	private final TypeImport typeImport;

	private final MethodName method;
	
	public ImportName(Context context, NamespaceReference namespace, ClassOrInterfaceName typeName) {
		super(context);
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(typeName);
		
		this.typeImport = new TypeImport(namespace.getParts(), typeName.getName());
		this.method = null;
	}
	
	public ImportName(Context context, String[] namespaceOrTypeName) {
		super(context);
		
		Objects.requireNonNull(namespaceOrTypeName);

		this.typeImport = new TypeImport(namespaceOrTypeName);
		this.method = null;
	}

	public ImportName(Context context, NamespaceReference namespace, ClassOrInterfaceName typeName, MethodName method) {
		super(context);
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(typeName);

		this.typeImport = new TypeImport(namespace.getParts(), typeName.getName(), method != null ? method.getName() : null);
		this.method = method;
	}

	private ImportName(Context context, TypeImport typeImport, MethodName method) {
		super(context);

		this.typeImport = typeImport;
		this.method = method;
	}

	public NamespaceReference getNamespace() {
		return new NamespaceReference(typeImport.getNamespace());
	}

	public ClassOrInterfaceName getTypeName() {
		return new ClassOrInterfaceName(typeImport.getTypeName());
	}

	public String[] getNamespaceOrTypeName() {
		return typeImport.getNamespaceOrTypeName();
	}

	public boolean isMethodImport() {
		return typeImport.isMethodImport();
	}

	public MethodName getMethod() {
		return method;
	}
	
	public void visit(TypeImportVisitor visitor) {
		typeImport.visit(visitor);
	}
	
	public boolean startsWith(String [] parts) {
	
		return typeImport.startsWith(parts);
	}
	
	public ImportName removeFromNamespace(String [] parts) {
		
		return new ImportName(getContext(), typeImport.removeFromNamespace(parts), method);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.IMPORT_NAME;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}

	@Override
	public String toString() {
		return typeImport.toString();
	}
}
