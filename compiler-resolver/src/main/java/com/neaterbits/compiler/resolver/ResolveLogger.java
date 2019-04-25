package com.neaterbits.compiler.resolver;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.ScopedName;

public final class ResolveLogger<COMPILATION_UNIT> {

	private final PrintStream out;

	public ResolveLogger(PrintStream out) {

		Objects.requireNonNull(out);

		this.out = out;
	}

	void onResolveFilesStart(Collection<CompiledFile<COMPILATION_UNIT>> files) {
		out.println("Start resolving " + files.size() + " files");
	}

	void onResolving(CompiledFile<COMPILATION_UNIT> file) {
		out.println("Resolving file " + file.getName());
	}
	
	void onResolveTypeStart(CompiledType type) {
		out.println("Resolving type " + type.getScopedName());
	}
	
	void onResolveTypeDependency(CompiledTypeDependency dependency, ResolvedType resolvedDependency) {
		out.println("  Resolve dependency " + dependency.getScopedName() + " to " + resolvedDependency);
	}
	
	void onResolveTypeEnd(ResolvedType type) {
		out.println("Resolved type " + (type != null ? type.getScopedName() : "null"));
	}

	void onTryResolveExtendsFrom(ScopedName scopedName, ResolvedType resolvedType) {
		out.println("  Try resolve extendsFrom " + scopedName + ": " + resolvedType);
	}
	
	void onResolveFilesEnd() {
		out.println("Done resolving files");
	}
}
