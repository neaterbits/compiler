package dev.nimbler.ide.ui.controller;

public interface UndoRedoBuffer {

	boolean hasUndoEntries();
	
	boolean hasRedoEntries();
}
