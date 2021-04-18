package dev.nimbler.ide.common.ui.keys;

import java.util.Collection;

import org.jutils.EnumMask;

public final class KeyMask extends EnumMask<QualifierKey> {

	public KeyMask(QualifierKey... values) {
		super(QualifierKey.class, values);
	}

	public KeyMask(Collection<QualifierKey> values) {
		super(QualifierKey.class, values);
	}
}
