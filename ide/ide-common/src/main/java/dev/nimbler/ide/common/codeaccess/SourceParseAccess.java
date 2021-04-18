package dev.nimbler.ide.common.codeaccess;

import java.util.function.Consumer;

import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.util.ui.text.Text;

public interface SourceParseAccess {

	void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel);
}
