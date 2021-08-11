package dev.nimbler.ide.model.text.difftextmodel;

import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextBuilder;

/**
 * Iterator for retrieving text from diff text model
 */
final class GetTextIterator implements DiffTextOffsetsIterator<GetTextIterator.GetTextState> {

    private static final Boolean DEBUG = Boolean.FALSE;
    
	static class GetTextState {
		private final Text initialText;
		private final TextBuilder textBuilder;
		
		GetTextState(Text initialText, TextBuilder textBuilder) {
			this.initialText = initialText;
			this.textBuilder = textBuilder;
		}
	}

	@Override
	public boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, GetTextState state) {

	    if (DEBUG) {
	        System.out.println("## onDiffText offset=" + offset.getText().asString() + ", length=" + offset.getNewLength());
	    }

		state.textBuilder.append(offset.getText());
		
		return true;
	}

	@Override
	public boolean onInitialModelText(long offsetIntoWholeText, long offsetIntoInitial, long lengthOfInitialText, GetTextState state) {

	    if (DEBUG) {
	        System.out.println("## onInitialModelText offset=" + offsetIntoInitial + ", length=" + lengthOfInitialText);
	    }

		state.textBuilder.append(state.initialText.substring(offsetIntoInitial, offsetIntoInitial + lengthOfInitialText));
		
		return true;
	}
}
