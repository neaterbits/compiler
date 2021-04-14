package dev.nimbler.ide.common.ui.translation;

public interface Translator {

	String translate(Translateable translateable);

	public static String getComponentNamespace(Class<?> cl) {
	    return cl.getName();
	}
}
