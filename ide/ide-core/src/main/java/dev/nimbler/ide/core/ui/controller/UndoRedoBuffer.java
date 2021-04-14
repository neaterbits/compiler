package dev.nimbler.ide.core.ui.controller;

public interface UndoRedoBuffer {

	boolean hasUndoEntries();
	
	boolean hasRedoEntries();
}
