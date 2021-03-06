package dev.nimbler.compiler.parser.listener.stackbased.state.base;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.util.parse.ParseLogger;

public class NamedListStackEntry<T> extends ListStackEntry<T> {

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
		
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.nameContext = nameContext;
	}
}
