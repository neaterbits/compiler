package dev.nimbler.ide.ui.actions.contexts;

import dev.nimbler.build.types.resource.SourceLineResourcePath;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

public class SearchResultContext extends ActionContext {

	private final SourceLineResourcePath sourceLine;

	public SearchResultContext(SourceLineResourcePath sourceLine) {
		this.sourceLine = sourceLine;
	}

	public SourceLineResourcePath getSourceLine() {
		return sourceLine;
	}
}
