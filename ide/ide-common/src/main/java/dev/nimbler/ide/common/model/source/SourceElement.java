package dev.nimbler.ide.common.model.source;

public interface SourceElement extends ISourceTokenProperties {

	long getStartOffset();
	
	long getLength();
}
