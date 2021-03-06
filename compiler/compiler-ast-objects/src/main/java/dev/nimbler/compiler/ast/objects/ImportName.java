package dev.nimbler.compiler.ast.objects;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassOrInterfaceName;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.types.imports.TypeImportVisitor;
import dev.nimbler.compiler.util.name.NamespaceReference;

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
		return typeImport.getNamespace() != null
		        ? new NamespaceReference(typeImport.getNamespace())
                : null;
	}

	public ClassOrInterfaceName getTypeName() {
		return typeImport.getTypeName() != null
		        ? new ClassOrInterfaceName(typeImport.getTypeName())
                : null;
	}

	public String[] getNamespaceOrTypeName() {
		return typeImport.getNamespaceOrTypeName();
	}
	
	public boolean isOnDemandImport() {
	    return typeImport.isOnDemandImport();
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
		return ParseTreeElement.IMPORT_NAME_PART;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}

	@Override
	public String toString() {
		return typeImport.toString();
	}
}
