package com.neaterbits.compiler.ast.parser;

import java.util.Objects;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class NamedListStackEntry<T extends BaseASTElement> extends ListStackEntry<T> {

	private String name;
	private Context nameContext;

	public NamedListStackEntry(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger);

		this.name = name;
		this.nameContext = nameContext;
	}

	public NamedListStackEntry(ParseLogger parseLogger) {
		super(parseLogger);

		this.name = null;
	}

	public final String getName() {
		return name;
	}
	
	public final Context getNameContext() {
		return nameContext;
	}

	public final void setName(String name, Context nameContext) {
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);
		
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.nameContext = nameContext;
	}
}
