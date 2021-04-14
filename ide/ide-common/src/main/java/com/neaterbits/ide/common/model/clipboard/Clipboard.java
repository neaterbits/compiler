package com.neaterbits.ide.common.model.clipboard;

public interface Clipboard {

	ClipboardData getData();
	
	ClipboardDataType getDataType();
	
	boolean hasDataType(ClipboardDataType dataType);
}
