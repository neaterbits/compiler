package dev.nimbler.compiler.util.parse;

import com.neaterbits.util.parse.context.Context;

public class CompileException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final Context context;
	
	public CompileException(Context context, String message) {
		super(message);
		
		this.context = context;
	}

	public final Context getContext() {
		return context;
	}
}