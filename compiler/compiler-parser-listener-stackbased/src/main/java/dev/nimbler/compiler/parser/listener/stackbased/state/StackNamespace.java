package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackNamespace<COMPILATION_CODE> extends ListStackEntry<COMPILATION_CODE> {

	private final String namespaceKeyword;
	private final Context namespaceKeywordContext;

	private final List<String> parts;
	private final List<Context> partContexts;

	public StackNamespace(
	        ParseLogger parseLogger,
	        String namespaceKeyword, Context namespaceKeywordContext) {

		super(parseLogger);

		this.namespaceKeyword = namespaceKeyword;
		this.namespaceKeywordContext = namespaceKeywordContext;

		this.parts = new ArrayList<>();
		this.partContexts = new ArrayList<>();
	}

	public String getNamespaceKeyword() {
		return namespaceKeyword;
	}

	public Context getNamespaceKeywordContext() {
		return namespaceKeywordContext;
	}

	public void addPart(String part, Context partContext) {

	    Objects.requireNonNull(part);
	    Objects.requireNonNull(partContext);

	    parts.add(part);
	    partContexts.add(partContext);
	}

	public List<String> getParts() {
		return parts;
	}

	public Context getNameContext() {

	    return partContexts.size() == 1
	            ? partContexts.get(0)
	            : Context.merge(partContexts, Function.identity());
	}
}
