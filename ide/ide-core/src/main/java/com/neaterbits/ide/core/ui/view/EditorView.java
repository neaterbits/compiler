package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextRange;

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
