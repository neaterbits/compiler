package dev.nimbler.ide.model.text.difftextmodel;

import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextBuilder;

final class GetTextRangeIterator implements DiffTextOffsetsIterator<GetTextRangeIterator.GetTextRangeState> {

	static class GetTextRangeState {
		private final long start;
		private final long length;
		private final Text initialText;
		private final TextBuilder textBuilder;

		GetTextRangeState(long start, long length, Text initialText, TextBuilder textBuilder) {
			
			this.start = start;
			this.length = length;
			this.initialText = initialText;
			this.textBuilder = textBuilder;
		}
		
		void appendIfInRange(long textPartIndex, Text text, long count, long rangeStart, long rangeLength) {
			
			final long textPartEndIndex = textPartIndex + count - 1;
			
			final long intersectingBeginIndex;
			final long intersectingLength;

			final Pos startPos = Pos.getPos(rangeStart, rangeLength, textPartIndex);
			final Pos endPos = Pos.getPos(rangeStart, rangeLength, textPartEndIndex);
			
			System.out.println("## startPos=" + startPos + ", endPos=" + endPos + ", textPartIndex=" + textPartIndex + ", textPartEndIndex=" + textPartEndIndex);
			
			switch (startPos) {
			case BEFORE:
				switch (endPos) {
				case BEFORE:
					intersectingBeginIndex = -1;
					intersectingLength = -1;
					break;
					
				case AT_START:
					intersectingBeginIndex = rangeStart;
					intersectingLength = rangeLength == 0 ? 0 : 1;
					break;
					
				case WITHIN:
					intersectingBeginIndex = rangeStart;
					intersectingLength = textPartEndIndex - rangeStart + 1;
					break;

				case AT_END:
				case AFTER:
					intersectingBeginIndex = rangeStart;
					intersectingLength = rangeLength;
					break;

				default:
					throw new UnsupportedOperationException();
				}
				break;
				
			case AT_START:
				switch (endPos) {
				case AT_START:
					intersectingBeginIndex = rangeStart;
					intersectingLength = 1;
					break;
					
				case WITHIN:
					intersectingBeginIndex = rangeStart;
					intersectingLength = textPartEndIndex - rangeStart + 1;
					break;

				case AT_END:
				case AFTER:
					intersectingBeginIndex = rangeStart;
					intersectingLength = rangeLength;
					break;

				default:
					throw new UnsupportedOperationException();
				}
				break;

			case WITHIN:
				switch (endPos) {
				case WITHIN:
					intersectingBeginIndex = textPartIndex;
					intersectingLength = textPartEndIndex - textPartIndex + 1;
					break;

				case AT_END:
				case AFTER:
					intersectingBeginIndex = textPartIndex;
					intersectingLength = (rangeStart + rangeLength) - textPartIndex;
					break;

				default:
					throw new UnsupportedOperationException();
				}
				break;
				
			case AT_END:
				switch (endPos) {

				case AT_END:
				case AFTER:
					intersectingBeginIndex = textPartIndex;
					intersectingLength = rangeLength == 0 ? 0 : 1;
					break;

				default:
					throw new UnsupportedOperationException();
				}
				break;
				
			case AFTER:
				intersectingBeginIndex = -1;
				intersectingLength = -1;
				break;
				
			default:
				throw new UnsupportedOperationException();
			}

			if (text.length() != count) {
				throw new IllegalStateException();
			}


			if (intersectingBeginIndex != -1) {
				
				final long indexIntoPart = intersectingBeginIndex - textPartIndex;
				
				final Text subString = text.substring(
						indexIntoPart,
						indexIntoPart + intersectingLength);
				
				textBuilder.append(subString);
			}
		}
	}

	@Override
	public boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, GetTextRangeState state) {
		
		state.appendIfInRange(offsetIntoWholeText, offset.getText(), offset.getNewLength(), state.start, state.length);
	
		return true;
	}

	@Override
	public boolean onInitialModelText(long offsetIntoWholeText, long offsetIntoInitial, long lengthOfInitialText, GetTextRangeState state) {
		
		final Text text = state.initialText.substring(offsetIntoInitial, offsetIntoInitial + lengthOfInitialText);
		
		state.appendIfInRange(offsetIntoWholeText, text, lengthOfInitialText, state.start, state.length);
		
		return true;
	}
}
