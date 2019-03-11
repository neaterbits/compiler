package com.neaterbits.compiler.ast;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.parser.ParsedFile;

public final class Program extends BaseASTElement {

	private final ASTList<Module> modules;
	
	public Program(List<Module> modules) {
		super(null);

		this.modules = makeList(modules);
	}

	public Program(Module module) {
		this(Arrays.asList(module));
	}
	
	public Program(ParsedFile parsedFile) {
		this(new Module(null, parsedFile));
	}

	public ASTList<Module> getModules() {
		return modules;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modules, recurseMode, iterator);
	}
}