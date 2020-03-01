package com.neaterbits.compiler.ast.objects;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class Program extends BaseASTElement {

	private final ASTList<Module> modules;
	
	public Program(List<Module> modules) {
		super(null);

		this.modules = makeList(modules);
	}

	public Program(Module module) {
		this(Arrays.asList(module));
	}
	
	public Program(ASTParsedFile parsedFile) {
		this(new Module(null, parsedFile));
	}

	public ASTList<Module> getModules() {
		return modules;
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PROGRAM;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modules, recurseMode, iterator);
	}
}
