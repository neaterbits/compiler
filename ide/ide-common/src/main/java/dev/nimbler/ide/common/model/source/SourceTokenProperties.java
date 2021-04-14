package dev.nimbler.ide.common.model.source;

import java.util.Objects;

class SourceTokenProperties implements ISourceTokenProperties {

	private final SourceElementMask flags;

	SourceTokenProperties(SourceElementMask flags) {
	
		Objects.requireNonNull(flags);
		
		this.flags = flags;
	}

	@Override
	public final boolean isRenameable() {
		return flags.isSet(SourceElementFlag.RENAMEABLE);
	}

	@Override
	public final boolean isHierarchyType() {
		return flags.isSet(SourceElementFlag.HIERARCHY_TYPE);
	}

	@Override
	public final boolean isReferenceable() {
		return flags.isSet(SourceElementFlag.REFERENCEABLE);
	}

	@Override
	public final boolean isMoveable() {
		return flags.isSet(SourceElementFlag.MOVEABLE);
	}
}
