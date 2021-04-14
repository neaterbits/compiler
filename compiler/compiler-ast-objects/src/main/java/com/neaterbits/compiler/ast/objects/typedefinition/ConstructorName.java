package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.ast.objects.block.CallableName;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.name.ClassName;
import com.neaterbits.util.parse.context.Context;

public final class ConstructorName extends CallableName {

	private final List<String> names;
	
	public ConstructorName(Context context, String name) {
		this(context, Arrays.asList(name));
	}
	
	public ConstructorName(Context context, ClassName className) {
		this(context, className.getName());
	}
	
	public ConstructorName(Context context, List<String> names) {
		super(context, names.get(names.size() - 1));
		
		this.names = Collections.unmodifiableList(names);
	}

	public List<String> getNames() {
		return names;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR_NAME;
	}
}
