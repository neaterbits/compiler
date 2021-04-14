package dev.nimbler.ide.model.text.difftextmodel;

interface DiffTextChangeConstAccess {

	Iterable<DiffTextOffset> getRemovedOffsets();
	
	Iterable<DiffTextOffset> getAddedOffsets();
	
}
