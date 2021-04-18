package dev.nimbler.ide.ui;

import java.util.List;

import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.styling.TextStyleOffset;

public interface TextStyling {

	List<TextStyleOffset> applyStylesToLine(long startPos, Text lineText, SourceFileModel sourceFileModel);

}
