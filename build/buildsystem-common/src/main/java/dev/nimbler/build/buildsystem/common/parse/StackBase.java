package dev.nimbler.build.buildsystem.common.parse;

import org.jutils.parse.context.Context;

public abstract class StackBase {

	private final Context context;

	protected StackBase(Context context) {

		// Objects.requireNonNull(context);

		this.context = context;
	}

	public final Context getContext() {
		return context;
	}
}
