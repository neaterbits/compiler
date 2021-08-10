package dev.nimbler.ide.util.ui.text;

import java.util.Objects;

import org.jutils.Value;

public interface Text {

	public static final Boolean DEBUG = false;
	
	public static final Text EMPTY_TEXT = new StringText("");
	
	public static Text replaceTextRange(Text text, long start, long replaceLength, Text toAdd) {

		final TextBuilder textBuilder = new CharText();
		
		textBuilder.append(text.substring(0, start))
			.append(toAdd)
			.append(text.substring(start + replaceLength));
		
		return textBuilder.toText();
	}

	default long findPosOfLineIndex(long lineIndexToFind, LineDelimiter lineDelimiter) {
		
		if (lineIndexToFind < 0) {
			throw new IllegalArgumentException();
		}
		
		long curPos = 0;
		long curLineIndex = 0;
		
		long foundPos = -1;
		
		while (curPos < length()) {
			
			if (curLineIndex == lineIndexToFind) {
				foundPos = curPos;
				break;
			}
			
			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(curPos, lineDelimiter);
			
			if (newlineChars > 0) {
				curPos += newlineChars;
				++ curLineIndex;
			}
			else {
				++ curPos;
			}
		}

		return foundPos;
	}

	default long findLineIndexAtPos(long posToFind, LineDelimiter lineDelimiter) {

		if (posToFind < 0) {
			throw new IllegalArgumentException();
		}
		
		if (posToFind >= length()) {
			throw new IllegalArgumentException();
		}
		
		long curPos = 0;
		long lineIndex = 0;
		
		for (curPos = 0; curPos <= posToFind;) {

			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(curPos, lineDelimiter);
			
			if (newlineChars > 0) {
				curPos += newlineChars;
				
				if (curPos > posToFind) {
					break;
				}
				
				++ lineIndex;
			}
			else {
				++ curPos;
			}
		}

		return lineIndex;
	}

	default long findNewline(long startPos, LineDelimiter lineDelimiter) {

		if (startPos < 0) {
			throw new IllegalArgumentException();
		}
		
		if (startPos >= length()) {
			throw new IllegalArgumentException();
		}
		
		long i = startPos;
		long offset = -1;
		
		while (i < length()) {
			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(i, lineDelimiter);
			
			if (newlineChars > 0) {
				offset = i;
				break;
			}
			else {
				++ i;
			}
		}
		
		return offset;
	}

	default long getNumberOfLinesIncludingTrailingNonTerminated(LineDelimiter lineDelimiter) {
		
		final Value<Long> value = new Value<>();
		
		final long numNewlines = getNumberOfNewlineChars(0L, lineDelimiter, value);
	
		final long numLines;
		
		if (value.get() != null && value.get() < length()) {
			numLines = numNewlines + 1;
		}
		else {
			numLines = numNewlines;
		}
		
		return numLines;
	}

    default long getNumberOfLinesWithoutTrailingNonTerminated(LineDelimiter lineDelimiter) {
        
        final long numNewlines = getNumberOfNewlineChars(0L, lineDelimiter, null);

        return numNewlines;
    }

	default long getNumberOfNewlineChars(LineDelimiter lineDelimiter) {
	    
		return getNumberOfNewlineChars(0L, lineDelimiter, null);
	}

    default long getNumberOfNewlineChars(LineDelimiter lineDelimiter, Value<Long> offsetAfterLastNewline) {
        
        return getNumberOfNewlineChars(0L, lineDelimiter, offsetAfterLastNewline);
    }
	
	default long getNumberOfNewlineChars(long startOffset, LineDelimiter lineDelimiter, Value<Long> offsetAfterLastNewline) {

		long i = startOffset;

		long numLines = 0;
		
		long offsetAfterLast = -1;
		
		for (; i < length();) {
			
			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(i, lineDelimiter);

			if (newlineChars > 0) {
				
				i += newlineChars;
				
				offsetAfterLast = i;
				
				++ numLines;
			}
			else {
				++ i;
			}
		}
		
		if (offsetAfterLast != -1 && offsetAfterLastNewline != null) {
			offsetAfterLastNewline.set(offsetAfterLast);
		}

		return numLines;
	}

	default int getNumberOfNewlineCharsForOneLineShift(long offset, LineDelimiter lineDelimiter) {

	    return lineDelimiter.getNumberOfNewlineCharsForOneLineShift(this, offset);
	}
	
	default Text getLineWithoutAnyNewline(long offset, LineDelimiter lineDelimiter) {
	    
	    final long posOfNewline = findNewline(offset, lineDelimiter);
	    
	    if (posOfNewline < 0L) {
	        throw new IllegalStateException();
	    }
	    
	    return substring(offset, posOfNewline);
	}
	
	default Text getLineWithoutAnyNewlineOrTillEndOfString(long offset, LineDelimiter lineDelimiter) {
	    
        final long posOfNewline = findNewline(offset, lineDelimiter);

        final Text result;
        
        if (posOfNewline < 0L) {
            result = offset == 0 ? this : substring(offset);
        }
        else {
            result = substring(offset, posOfNewline);
        }
        
        return result;
	}
	    
	boolean isEmpty();
	
	long length();
	
	char charAt(long index);
	
	Text substring(long beginIndex);

	Text substring(long beginIndex, long endIndex);

	Text merge(Text other);

	Text merge(String other);

	default boolean startsWith(String string) {

       return startsWith(new StringText(string));
	}

	default boolean startsWith(Text string) {
        
        Objects.requireNonNull(string);
        
        boolean startsWith;
        
        if (string.isEmpty()) {
            startsWith = true;
        }
        else {
            final long textLength = length();
            final long stringLength = string.length();
            
            if (textLength < string.length()) {
                startsWith = false;
            }
            else {
                startsWith = true;
    
                for (int i = 0; i < stringLength; ++ i) {
                    if (charAt(i) != string.charAt(i)) {
                        startsWith = false;
                        break;
                    }
                }
            }
        }
    
        return startsWith;
    }

	default boolean endsWith(String string) {
	    
	    Objects.requireNonNull(string);
	    
        boolean endsWith;
        
	    if (string.isEmpty()) {
	        endsWith = true;
	    }
	    else {
    	    
    	    final long textLength = length();
    	    final int stringLength = string.length();
    	    
    	    if (textLength < string.length()) {
    	        endsWith = false;
    	    }
    	    else {
    	        endsWith = true;
    
    	        for (int i = 0; i < stringLength; ++ i) {
    	            if (charAt(textLength - i - 1) != string.charAt(stringLength - i - 1)) {
    	                endsWith = false;
    	                break;
    	            }
    	        }
    	    }
	    }
    
	    return endsWith;
	}

	default Text initial(long count) {

        final long length = length();

        if (length < 0) {
               throw new IllegalArgumentException();
        }
        else if (length < count) {
            throw new IllegalArgumentException();
        }

        return substring(0, count);
    }

	default Text trailing(long count) {
	    
	    final long length = length();

	    if (length < 0) {
	           throw new IllegalArgumentException();
	    }
	    else if (length < count) {
	        throw new IllegalArgumentException();
	    }
	    
        final long startIdx = length - count;
	    
	    return substring(startIdx, startIdx + count);
	}

	default Text toLowerCase() {

		final CharText text = new CharText(length());
		
		for (long i = 0; i < length(); ++ i) {
			text.append(Character.toLowerCase(charAt(i)));
		}
		
		return text;
	}
	
	default long findBeginningOfNextLine(long offset, LineDelimiter lineDelimiter) {
		
		if (offset < 0) {
			throw new IllegalArgumentException();
		}

		if (offset >= length()) {
			throw new IllegalArgumentException();
		}

		final long newlineOffset = findNewline(offset, lineDelimiter);
		
		final long result;
		
		if (newlineOffset == -1) {
			result = -1;
		}
		else {
			
			final long newlineChars = lineDelimiter.getNumberOfNewlineCharsForOneLineShift(this, newlineOffset);
			
			if (newlineOffset + newlineChars >= length()) {
				result = -1;
			}
			else {
				result = newlineOffset + newlineChars;
			}
		}
		
		return result;
	}
	
	String asString();

	default long findForward(String searchText, long start, TextRange range, boolean caseSensitive, boolean wrapSearch, boolean wholeWord) {
		return findForward(
				new StringText(searchText),
				start,
				range,
				caseSensitive,
				wrapSearch,wholeWord);
	}

	default long findForward(Text searchText, long start, TextRange range, boolean caseSensitive, boolean wrapSearch, boolean wholeWord) {
		
		if (start < 0) {
			throw new IllegalArgumentException();
		}
		
		final TextRange rangeToSearch;
		
		if (range == null) {
			rangeToSearch = new TextRange(0L, length());
		}
		else {
			
			if (range.getOffset() + range.getLength() > length()) {
				throw new IllegalArgumentException();
			}
			
			rangeToSearch = range;
		}
		
		if (!rangeToSearch.containsPos(start)) {
			throw new IllegalArgumentException();
		}
		
		Objects.requireNonNull(searchText);
		
		if (searchText.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		if (wholeWord) {
			TextUtil.checkIsNoWhiteSpace(searchText);
		}
		
		long foundPos;
		
		final long searchTextLength = searchText.length();

		Text lowercaseSearchText = null;

		if (searchTextLength > rangeToSearch.getLength()) {
			foundPos = -1L;
		}
		else if (searchTextLength > rangeToSearch.getLength() - start) {
			
			if (wrapSearch) {
				final long wrapEndPos = start - 1;

				if (!caseSensitive) {
					lowercaseSearchText = searchText.toLowerCase();
				}

				foundPos = TextUtil.searchForward(
						0L, wrapEndPos,
						rangeToSearch,
						searchTextLength,
						caseSensitive, wholeWord, searchText, lowercaseSearchText, this::charAt);
				
			}
			else {
				foundPos = -1L;
			}
		}
		else {
			if (!caseSensitive) {
				lowercaseSearchText = searchText.toLowerCase();
			}

			final long endPos = rangeToSearch.getOffset() + rangeToSearch.getLength() - searchTextLength;
			
			foundPos = TextUtil.searchForward(
					start, endPos,
					rangeToSearch,
					searchTextLength,
					caseSensitive, wholeWord,
					searchText, lowercaseSearchText,
					this::charAt);
		
			if (foundPos == -1 && wrapSearch && start >= searchTextLength) {
				final long wrapEndPos = start - 1;

				foundPos = TextUtil.searchForward(
						rangeToSearch.getOffset(), wrapEndPos,
						rangeToSearch,
						searchTextLength,
						caseSensitive, wholeWord, searchText, lowercaseSearchText, this::charAt);

			}
		}
		
		return foundPos;
	}
	
	default long findBackward(String searchText, long startPosForSearch, TextRange range, boolean caseSensitive, boolean wrapSearch, boolean wholeWord) {
		return findBackward(
				new StringText(searchText),
				startPosForSearch,
				range,
				caseSensitive,
				wrapSearch,wholeWord);
	}

	default long findBackward(Text searchText, long startPosForSearch, TextRange range, boolean caseSensitive, boolean wrapSearch, boolean wholeWord) {
		
		if (startPosForSearch < 0) {
			throw new IllegalArgumentException();
		}
		
		final TextRange rangeToSearch;
		
		if (range == null) {
			rangeToSearch = new TextRange(0L, length());
		}
		else {
			
			if (range.getLength() > startPosForSearch + 1) {
				throw new IllegalArgumentException();
			}
			
			if (range.getOffset() + range.getLength() > length()) {
				throw new IllegalArgumentException();
			}
			
			rangeToSearch = range;
		}
		
		if (startPosForSearch > length()) {
			throw new IllegalArgumentException();
		}

		if (!rangeToSearch.containsPosOrEndIndex(startPosForSearch)) {
			throw new IllegalArgumentException();
		}
		
		Objects.requireNonNull(searchText);
		
		if (searchText.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		if (wholeWord) {
			TextUtil.checkIsNoWhiteSpace(searchText);
		}
		
		long foundPos;
		final long searchTextLength = searchText.length();
		Text lowercaseSearchText = null;

		if (searchTextLength > rangeToSearch.getLength()) {
			// text to search for longer than text so just return -1
			foundPos = -1L;
		}
		else if (searchTextLength > startPosForSearch) {
			
			if (DEBUG) {
				System.out.println("Text.findBackwards from at start of text");
			}
			
			// at very beginning of search text so wrap around immediately
			if (wrapSearch) {
				
				final long wrapEndPos = rangeToSearch.getLength() - searchTextLength - 1;

				if (!caseSensitive) {
					lowercaseSearchText = searchText.toLowerCase();
				}

				foundPos = TextUtil.searchBackward(
						0L, wrapEndPos,
						rangeToSearch,
						searchTextLength,
						caseSensitive, wholeWord, searchText, lowercaseSearchText, this::charAt);
				
			}
			else {
				foundPos = -1L;
			}
		}
		else {
			if (!caseSensitive) {
				lowercaseSearchText = searchText.toLowerCase();
			}

			if (DEBUG) {
				System.out.println("Text.findBackwards from start point of text");
			}

			// search from start point backwards
			foundPos = TextUtil.searchBackward(
					rangeToSearch.getOffset(), startPosForSearch - searchTextLength,

					rangeToSearch,
					
					searchTextLength,
					caseSensitive, wholeWord,
					searchText, lowercaseSearchText,
					this::charAt);
		
			if (foundPos == -1 && wrapSearch && startPosForSearch >= searchTextLength) {
				
				final long wrapEndPos = startPosForSearch - searchTextLength;

				if (DEBUG) {
					System.out.println("Text.findBackwards from wrapped point of text " + wrapEndPos + " to " + startPosForSearch);
				}
				
				foundPos = TextUtil.searchBackward(
						startPosForSearch + 1, rangeToSearch.getOffset() + rangeToSearch.getLength() - searchTextLength,
						rangeToSearch,
						searchTextLength,
						caseSensitive, wholeWord, searchText, lowercaseSearchText, this::charAt);
			}
		}
		
		return foundPos;
	}
}
