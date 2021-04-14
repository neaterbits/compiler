package com.neaterbits.ide.common.model.clipboard;

import java.util.Objects;

public final class ClipboardDataType {

	public static final ClipboardDataType TEXT = new ClipboardDataType(TextClipboardData.class);
	
	private final Class<? extends ClipboardData> type;

	public ClipboardDataType(Class<? extends ClipboardData> type) {

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public Class<? extends ClipboardData> getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClipboardDataType other = (ClipboardDataType) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
