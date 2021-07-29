package dev.nimbler.ide.component.common.action;

import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.component.common.language.Languages;

public abstract class BaseActionComponentAppParameters
    extends BaseActionComponentParameters
    implements ActionAppParameters {

    protected BaseActionComponentAppParameters(CodeAccess codeAccess, Languages languages) {
        super(codeAccess, languages);
    }
}
