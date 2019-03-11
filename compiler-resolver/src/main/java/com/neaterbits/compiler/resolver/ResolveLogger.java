package com.neaterbits.compiler.resolver;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.ScopedName;

public final class ResolveLogger<BUILTINTYPE, COMPLEXTYPE> {

	private final PrintStream out;

	public ResolveLogger(PrintStream out) {

		Objects.requireNonNull(out);

		this.out = out;
	}

	void onResolveFilesStart(Collection<CompiledFile<COMPLEXTYPE>> files) {
		out.println("Start resolving " + files.size() + " files");
	}

	void onResolving(CompiledFile<COMPLEXTYPE> file) {
		out.println("Resolving file " + file.getName());
	}
	
	void onResolveTypeStart(CompiledType<COMPLEXTYPE> type) {
		out.println("Resolving type " + type.getScopedName());
	}
	
	void onResolveTypeDependency(CompiledTypeDependency dependency, ResolvedType<BUILTINTYPE, COMPLEXTYPE> resolvedDependency) {
		out.println("  Resolve dependency " + dependency.getScopedName() + " to " + resolvedDependency);
	}
	
	void onResolveTypeEnd(ResolvedType<BUILTINTYPE, COMPLEXTYPE> type) {
		out.println("Resolved type " + (type != null ? type.getScopedName() : "null"));
	}

	void onTryResolveExtendsFrom(ScopedName scopedName, ResolvedType<BUILTINTYPE, COMPLEXTYPE> resolvedType) {
		out.println("  Try resolve extendsFrom " + scopedName + ": " + resolvedType);
	}
	
	void onResolveFilesEnd() {
		out.println("Done resolving files");
	}
}
