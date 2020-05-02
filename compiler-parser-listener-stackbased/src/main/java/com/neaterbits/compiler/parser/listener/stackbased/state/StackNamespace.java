package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
	    
	    return Context.merge(partContexts, Function.identity());
	}
}
