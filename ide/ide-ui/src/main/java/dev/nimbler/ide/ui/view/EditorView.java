package dev.nimbler.ide.ui.view;

import dev.nimbler.ide.common.ui.view.View;
import dev.nimbler.ide.model.text.TextModel;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextRange;

public interface EditorView extends View {

	Text getText();

	void setTextModel(TextModel textModel);

	void triggerTextRefresh();
	
	void addTextChangeListener(TextEditorChangeListener listener);

	void addCursorPositionListener(CursorPositionListener cursorPositionListener);
	
	long getCursorPosition();

	void setCursorPosition(long offset);
	
	TextRange getSelection();
	
	void addDisposeListener(ViewDisposeListener listener);
	
	void selectAll();
	
	void select(long offset, long length);

	void triggerStylingRefresh();

	void addKeyListener(KeyEventListener listener);
}
