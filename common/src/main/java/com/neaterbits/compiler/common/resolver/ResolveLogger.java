package com.neaterbits.compiler.common.resolver;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.ResolvedType;

public final class ResolveLogger {

	private final PrintStream out;

	public ResolveLogger(PrintStream out) {

		Objects.requireNonNull(out);

		this.out = out;
	}

	void onResolveFilesStart(Collection<CompiledFile> files) {
		out.println("Start resolving " + files.size() + " files");
	}

	void onResolving(CompiledFile file) {
		out.println("Resolving file " + file.getName());
	}
	
	void onResolveType(CompiledType type) {
		out.println("Resolving type " + type.getScopedName());
	}

	void onTryResolveExtendsFrom(ScopedName scopedName, ResolvedType resolvedType) {
		out.println("Resolved " + scopedName + ": " + resolvedType);
	}
	
	void onResolveFilesEnd() {
		out.println("Done resolving files");
	}
}
