package com.neaterbits.ide.model.text.difftextmodel;

import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextBuilder;

final class GetTextIterator implements DiffTextOffsetsIterator<GetTextIterator.GetTextState> {
	
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

		System.out.println("## onDiffText offset=" + offset.getText().asString() + ", length=" + offset.getNewLength());

		state.textBuilder.append(offset.getText());
		
		return true;
	}

	@Override
	public boolean onInitialModelText(long offsetIntoWholeText, long offsetIntoInitial, long lengthOfInitialText, GetTextState state) {
		
		System.out.println("## onInitialModelText offset=" + offsetIntoInitial + ", length=" + lengthOfInitialText);
		
		state.textBuilder.append(state.initialText.substring(offsetIntoInitial, offsetIntoInitial + lengthOfInitialText));
		
		return true;
	}
}
