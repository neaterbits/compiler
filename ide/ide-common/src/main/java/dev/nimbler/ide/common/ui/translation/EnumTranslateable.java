package dev.nimbler.ide.common.ui.translation;

public interface EnumTranslateable<T extends Enum<T>> extends Translateable {

	default String getTranslationId(T enumeration) {
		
		@SuppressWarnings("unchecked")
		final Class<T> enumClass = (Class<T>)getClass();
		
		return enumClass.getEnumConstants()[enumeration.ordinal()].name().toLowerCase();
	}
}
