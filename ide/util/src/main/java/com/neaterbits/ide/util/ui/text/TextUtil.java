package com.neaterbits.ide.util.ui.text;

class TextUtil {

	@FunctionalInterface
	interface CharAt {
		char charAt(long offset);
	}
	
	static void checkIsNoWhiteSpace(Text searchText) {
		for (int i = 0; i < searchText.length(); ++ i) {
			if (Character.isWhitespace(searchText.charAt(i))) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	static long searchForward(
			long searchStartPos,
			long searchEndPos,
			TextRange searchArea,
			long searchTextLength,
			boolean caseSensitive, boolean wholeWord,
			Text searchText, Text lowercaseSearchText,
			CharAt text) {

		long foundPos = -1L;
		
		for (long pos = searchStartPos; pos <= searchEndPos; ++ pos) {
			
			final boolean match = matchSearchText(pos, searchTextLength, caseSensitive, searchText, lowercaseSearchText, text);

			if (match) {
				
				if (wholeWord) {
					if (isWholeWord(pos, searchArea, searchTextLength, text)) {
						foundPos = pos;
						break;
					}
				}
				else {
					foundPos = pos;
					break;
				}
			}
		}
		
		return foundPos;
	}

	static long searchBackward(
			long searchStartPos,
			long searchEndPos,
			TextRange searchArea,
			long searchTextLength,
			boolean caseSensitive, boolean wholeWord,
			Text searchText, Text lowercaseSearchText,
			CharAt text) {

		long foundPos = -1L;
		
		if (searchStartPos > searchEndPos) {
			throw new IllegalArgumentException();
		}
		
		if (searchStartPos < searchArea.getOffset()) {
			throw new IllegalArgumentException();
		}
		
		if (searchEndPos > searchArea.getOffset() + searchArea.getLength()) {
			throw new IllegalArgumentException();
		}
		
		for (long pos = searchEndPos; pos >= searchStartPos; -- pos) {
			
			final boolean match = matchSearchText(pos, searchTextLength, caseSensitive, searchText, lowercaseSearchText, text);

			if (match) {
				
				if (wholeWord) {
					if (isWholeWord(pos, searchArea, searchTextLength, text)) {
						foundPos = pos;
						break;
					}
				}
				else {
					foundPos = pos;
					break;
				}
			}
		}
		
		return foundPos;
	}

	private static boolean matchSearchText(long pos, long searchTextLength, boolean caseSensitive, Text searchText, Text lowercaseSearchText, CharAt text) {

		boolean match = true;
		
		for (long i = 0; match && i < searchTextLength; ++ i) {
			
			final char c = text.charAt(pos + i);
			
			if (caseSensitive) {
				match = c == searchText.charAt(i);
			}
			else {
				match = Character.toLowerCase(c) == lowercaseSearchText.charAt(i);
			}
		}

		return match;
	}

	private static boolean isWholeWord(long pos, TextRange searchArea, long searchTextLength, CharAt text) {
		return isWholeWord(pos, searchArea.getOffset(), searchArea.getLength(), searchTextLength, text);
	}

	private static boolean isWholeWord(long pos, long start, long length, long searchTextLength, CharAt text) {
		
		final boolean isWholeWord;
		
		if (pos + searchTextLength > length) {
			throw new IllegalStateException();
		}
		else if (length == searchTextLength) {
			isWholeWord = true;
		}
		else if (pos + searchTextLength == length) {
			isWholeWord = Character.isWhitespace(text.charAt(pos - 1));
		}
		else if (pos == start) {
			isWholeWord = Character.isWhitespace(text.charAt(pos + searchTextLength));
		}
		else {
			isWholeWord = Character.isWhitespace(text.charAt(pos - 1)) && Character.isWhitespace(text.charAt(pos + searchTextLength));
		}
		
		return isWholeWord;
	}
}
