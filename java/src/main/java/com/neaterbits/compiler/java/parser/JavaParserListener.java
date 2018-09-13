package com.neaterbits.compiler.java.parser;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.antlr4.ModelParserListener;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.parser.iterative.oo.BaseIterativeOOParserListener;
import com.neaterbits.compiler.java.ast.JavaProgram;

public final class JavaParserListener extends BaseIterativeOOParserListener
	implements ModelParserListener<JavaProgram>{

	// Cache packagename
	private String packageName;
	
	@Override
	public JavaProgram getResult() {
		return null;
	}

	public void onPackageDeclaration(Context context, String name) {
		this.packageName = name;
		
		super.onNamespaceStart();
	}

	@Override
	public CompilationUnit onCompilationUnitEnd(Context context) {
		
		// Trigger namespace end here since namespace contains code
		// to suppert eg. C# namespace { }, namespace { }
		super.onNameSpaceEnd(context, packageName);
		
		return super.onCompilationUnitEnd(context);
	}
}
