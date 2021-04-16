package dev.nimbler.ide.component.common.action;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.component.common.language.Languages;

public abstract class BaseActionComponentAppParameters
    extends BaseActionComponentParameters
    implements ActionAppParameters {

    protected BaseActionComponentAppParameters(BuildRoot buildRoot, Languages languages) {
        super(buildRoot, languages);
    }
}
