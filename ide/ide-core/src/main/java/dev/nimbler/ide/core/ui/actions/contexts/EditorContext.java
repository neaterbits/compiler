package dev.nimbler.ide.core.ui.actions.contexts;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

public final class EditorContext extends ActionContext {

	private final SourceFileResourcePath sourceFile;

	public EditorContext(SourceFileResourcePath sourceFile) {
	
		Objects.requireNonNull(sourceFile);
		
		this.sourceFile = sourceFile;
	}

	public SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}
}
