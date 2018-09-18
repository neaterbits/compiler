package com.neaterbits.compiler.common.ast;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;

public final class Import extends BaseASTElement {

	private final NamespaceName namespace;
	private final ClassName className;
	private final String [] packageOrTypeName;
	private final boolean methodImport;
	private final MethodName method;

	public Import(Context context, NamespaceName namespace, ClassName className) {
		super(context);
		
		this.namespace = namespace;
		this.className = className;
		this.packageOrTypeName = null;
		this.methodImport = false;
		this.method = null;
	}
	
	public Import(Context context, String[] packageOrTypeName) {
		super(context);
		this.namespace = null;
		this.className = null;
		this.packageOrTypeName = packageOrTypeName;
		this.methodImport = false;
		this.method = null;
	}

	public Import(Context context, NamespaceName namespace, ClassName className, MethodName method) {
		super(context);
		
		Objects.requireNonNull(namespace);
		
		this.namespace = namespace;
		this.className = className;
		this.packageOrTypeName = null;
		this.methodImport = true;
		this.method = method;
	}

	public NamespaceName getNamespace() {
		return namespace;
	}

	public ClassName getClassName() {
		return className;
	}

	public String[] getPackageOrTypeName() {
		return packageOrTypeName;
	}

	public boolean isMethodImport() {
		return methodImport;
	}

	public MethodName getMethod() {
		return method;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
