package com.neaterbits.compiler.common.ast;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.parser.ParsedFile;

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
		this(new Module(parsedFile));
	}

	public ASTList<Module> getModules() {
		return modules;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(modules, recurseMode, visitor);
	}
}
