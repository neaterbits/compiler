package dev.nimbler.ide.model.text.difftextmodel;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.LongArray;
import dev.nimbler.ide.util.ui.text.Text;

final class LinesFinder {
	
	private static final int INITIAL_LENGTH = 10000;
	
	private long numLines = 0;
	private LongArray offsets = new LongArray(INITIAL_LENGTH, INITIAL_LENGTH);
	private LongArray lengths = new LongArray(INITIAL_LENGTH, INITIAL_LENGTH);
	

	private void setLine(long lineIndex, long offset, long length) {
		
		// System.out.println("## lineIndex " + lineIndex + " offset " + offset + " length " + length);
		
		offsets.set(lineIndex, offset);
		lengths.set(lineIndex, length);
	}
	
	private void addLine(long startOfLine, long startOfNextLine) {
		setLine(numLines ++, startOfLine, startOfNextLine - startOfLine);
	}

	
	static LinesOffsets findLineOffsets(Text text, LineDelimiter lineDelimiter) {
		
		final LinesFinder linesFinder = new LinesFinder();

		long offsetAfterLastNewline = -1;
		
		for (long i = 0; i < text.length();) {
			
			final long newlineChars = text.getNumberOfNewlineCharsForOneLineShift(i, lineDelimiter);
			
			if (newlineChars > 0) {
				
				i += newlineChars;

				linesFinder.addLine(
						offsetAfterLastNewline == -1
							? 0
							: offsetAfterLastNewline,
						i);
				
				
				offsetAfterLastNewline = i;
			}
			else {
				++ i;
			}
		}
		
		if (offsetAfterLastNewline == -1) {
			linesFinder.addLine(0, text.length());
		}
		else {
			if (offsetAfterLastNewline < text.length()) {
				linesFinder.addLine(offsetAfterLastNewline, text.length());
			}
		}

		return new LinesOffsets(linesFinder.numLines, text.length(), linesFinder.offsets, linesFinder.lengths);
	}
}
