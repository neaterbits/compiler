package com.neaterbits.ide.common.ui.translation;

public interface Translateable {

	String getTranslationNamespace();
	
	String getTranslationId();
	
	public static Translateable fromComponent(String translationId, Class<?> componentType) {
	    
	    return new Translateable() {
            
            @Override
            public String getTranslationNamespace() {
                return Translator.getComponentNamespace(componentType);
            }
            
            @Override
            public String getTranslationId() {
                return translationId;
            }
        };
	}
}
