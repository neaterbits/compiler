package com.neaterbits.compiler.java.parser.antlr4;

import com.neaterbits.compiler.java.Java8BaseListener;
import com.neaterbits.compiler.java.Java8Parser.CompilationUnitContext;
import com.neaterbits.compiler.java.Java8Parser.NormalClassDeclarationContext;
import com.neaterbits.compiler.java.Java8Parser.PackageNameContext;
import com.neaterbits.compiler.java.parser.JavaParserListener;

import static com.neaterbits.compiler.common.antlr4.Antlr4.context;

public class Java8AntlrParserListener extends Java8BaseListener {

	private final JavaParserListener delegate;

	public Java8AntlrParserListener(JavaParserListener delegate) {
		this.delegate = delegate;
	}

	@Override
	public void enterCompilationUnit(CompilationUnitContext ctx) {
		delegate.onCompilationUnitStart();
	}

	@Override
	public void exitCompilationUnit(CompilationUnitContext ctx) {
		delegate.onCompilationUnitEnd(context(ctx));
	}

	@Override
	public void exitPackageName(PackageNameContext ctx) {
		delegate.onPackageDeclaration(context(ctx), ctx.getText());
	}

	@Override
	public void enterNormalClassDeclaration(NormalClassDeclarationContext ctx) {
		delegate.onClassStart(ctx.name.getText());
	}

	@Override
	public void exitNormalClassDeclaration(NormalClassDeclarationContext ctx) {
		delegate.onClassEnd(context(ctx));
	}
}
