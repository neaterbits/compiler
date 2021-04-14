package com.neaterbits.ide.core.ui.controller;

public interface UndoRedoBuffer {

	boolean hasUndoEntries();
	
	boolean hasRedoEntries();
}
