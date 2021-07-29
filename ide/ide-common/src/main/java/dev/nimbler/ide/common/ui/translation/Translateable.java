package dev.nimbler.ide.common.ui.translation;

public interface Translateable {

	String getTranslationNamespace();
	
	String getTranslationId();
	
	public static Translateable makeTranslateable(String translationNamespace, String translationId) {
	    
        return new Translateable() {
            
            @Override
            public String getTranslationNamespace() {
                return translationNamespace;
            }
            
            @Override
            public String getTranslationId() {
                return translationId;
            }
        };
	}
	
	public static Translateable fromComponent(String translationId, Class<?> componentType) {
	    
	    return makeTranslateable(Translator.getComponentNamespace(componentType), translationId);
	}
}
