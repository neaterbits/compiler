package dev.nimbler.ide.common.model.source;

import java.util.Collection;

import org.jutils.EnumMask;

final class SourceElementMask extends EnumMask<SourceElementFlag> {

	SourceElementMask(Collection<SourceElementFlag> values) {
		super(SourceElementFlag.class, values);
	}

	SourceElementMask(SourceElementFlag... values) {
		super(SourceElementFlag.class, values);
	}
}
