package com.neaterbits.ide.main;

import com.neaterbits.ide.common.ui.translation.Translateable;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.component.build.ui.BuildIssuesComponent;
import com.neaterbits.ide.component.common.ui.ComponentUI;
import com.neaterbits.ide.component.common.ui.DetailsComponentUI;
import com.neaterbits.ide.component.compiledfiledebug.ui.CompiledFileViewComponent;
import com.neaterbits.ide.core.ui.controller.IDECoreTranslator;

final class IDEMainTranslator implements Translator {

    private final IDECoreTranslator coreTranslator;
    
    IDEMainTranslator() {

        this.coreTranslator = new IDECoreTranslator();
    }
    
    private static boolean isComponentNamespace(Translateable translateable, Class<? extends ComponentUI> component) {
        
        return translateable.getTranslationNamespace()
                .equals(Translator.getComponentNamespace(component));
    }

    @Override
    public String translate(Translateable translateable) {

        final String translation;
        
        if (isComponentNamespace(translateable, BuildIssuesComponent.class)) {
            
            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Build Issues";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else if (isComponentNamespace(translateable, CompiledFileViewComponent.class)) {

            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Compiled File";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else {
            translation = coreTranslator.translate(translateable);
        }
        
        return translation;
    }
}
