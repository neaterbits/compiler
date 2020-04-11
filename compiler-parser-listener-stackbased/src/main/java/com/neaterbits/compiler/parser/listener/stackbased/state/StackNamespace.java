package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.NamedListStackEntry;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackNamespace<COMPILATION_CODE> extends NamedListStackEntry<COMPILATION_CODE> {

	private final String namespaceKeyword;
	private final Context namespaceKeywordContext;
	
	private final List<String> parts;
	
	public StackNamespace(
	        ParseLogger parseLogger,
	        String namespaceKeyword, Context namespaceKeywordContext,
	        String name, Context nameContext) {
	    
		super(parseLogger, name, nameContext);

		this.namespaceKeyword = namespaceKeyword;
		this.namespaceKeywordContext = namespaceKeywordContext;
		
		this.parts = new ArrayList<>();
	}

	public String getNamespaceKeyword() {
		return namespaceKeyword;
	}

	public Context getNamespaceKeywordContext() {
		return namespaceKeywordContext;
	}
	
	public void addPart(String part) {
	    
	    Objects.requireNonNull(part);

	    parts.add(part);
	}

	public List<String> getParts() {
		return parts;
	}
}
