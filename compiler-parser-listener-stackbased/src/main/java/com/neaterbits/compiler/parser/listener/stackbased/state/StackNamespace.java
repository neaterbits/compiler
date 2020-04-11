package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Arrays;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.NamedListStackEntry;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackNamespace<COMPILATION_CODE> extends NamedListStackEntry<COMPILATION_CODE> {

	private final String namespaceKeyword;
	private final Context namespaceKeywordContext;
	
	private final String [] parts;
	
	public StackNamespace(ParseLogger parseLogger, String namespaceKeyword, Context namespaceKeywordContext, String name, Context nameContext, String [] parts) {
		super(parseLogger, name, nameContext);

		this.namespaceKeyword = namespaceKeyword;
		this.namespaceKeywordContext = namespaceKeywordContext;
		
		this.parts = Arrays.copyOf(parts, parts.length);
	}

	public String getNamespaceKeyword() {
		return namespaceKeyword;
	}

	public Context getNamespaceKeywordContext() {
		return namespaceKeywordContext;
	}

	public String[] getParts() {
		return parts;
	}
}