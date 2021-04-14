package com.neaterbits.ide.swt;

import com.neaterbits.ide.util.ui.text.Text;

final class TextDiffer {

	private Text currentText;
	
	TextDiffer(Text currentText) {
		this.currentText = currentText;
	}

	ReplaceText computeReplaceText(Text updatedText) {
		
		final long length;
		
		if (currentText.length() > updatedText.length()) {
			length = updatedText.length();
		}
		else {
			length = currentText.length();
		}
		
		long diffAt = -1;
		
		for (long i = 0; i < length; ++ i) {
			if (currentText.charAt(i) != updatedText.charAt(i)) {
				diffAt = i;
				break;
			}
		}
		
		// System.out.println("## found diff at " + diffAt);
		
		long replaceStart = -1;
		long replaceLength = -1;

		Text removedText = null;
		Text addedText = null;
		
		if (diffAt != -1) {

			for (int i = 0; i < length; ++ i) {
				
				final long currentPos = currentText.length() - i - 1;
				final long updatedPos = updatedText.length() - i - 1;

				// System.out.println("## comparing current " + currentPos + " to updated " + updatedPos);
				
				if (currentPos < diffAt || updatedPos < diffAt || currentText.charAt(currentPos) != updatedText.charAt(updatedPos)) {
					
					// System.out.println("## diff at current " + currentPos + " to updated " + updatedPos);
					
					replaceStart = diffAt;
					
					/*
					if (updatedPos > currentPos) {
						replaceLength = updatedPos - diffAt + 1;
					}
					else {
						replaceLength = currentPos - diffAt + 1;
					}
					*/
					
					replaceLength = currentPos - diffAt + 1;
					
					// System.out.println("### diffAt " + diffAt + ", currenttext " + currentText.length() + ", i=" + i + ", \"" + currentText + "\"");
					
					removedText = currentText.substring(diffAt, currentText.length() - i);
					addedText = updatedText.substring(diffAt, updatedText.length() - i);
					break;
				}
				else if (updatedPos == 0 && currentPos != 0) {
					
					replaceStart = diffAt;
					replaceLength = currentPos - updatedPos;
					removedText = currentText.substring(diffAt, currentPos);
					addedText = updatedText.substring(diffAt, updatedPos);
					break;
				}
				else if (updatedPos != 0 && currentPos == 0) {
					
					replaceStart = diffAt;
					replaceLength = 0;
					removedText = currentText.substring(diffAt, currentPos);
					addedText = updatedText.substring(diffAt, updatedPos);
					break;
				}
			}
		}
		else if (updatedText.length() > currentText.length()) {
			
			replaceStart = currentText.length();
			replaceLength = 0;
			addedText = updatedText.substring(currentText.length());
		}
		else {
		}
		
		// System.out.println("## removedText \"" + removedText + "\"" + ", addedText \"" + addedText + "\", replaceLength=" + replaceLength);
		
		if (replaceLength > 0 && replaceLength != removedText.length()) {
			throw new IllegalStateException("diff " + replaceLength + "/" + removedText.length());
		}
		
		return new ReplaceText(replaceStart, replaceLength, removedText, addedText);
	}
	
		
	ReplaceTextRange computeReplaceTextRange(Text updatedText) {
		
		final ReplaceText replaceText = computeReplaceText(updatedText);
		
		final ReplaceTextRange replaceTextRange;
		
		final Text removedText = replaceText.getRemovedText();
		final Text addedText = replaceText.getAddedText();
		
		
		if (   (removedText != null && !removedText.isEmpty() && containsWhitespace(removedText) && containsNonWhitespace(removedText))
			|| (addedText   != null && !addedText.isEmpty()   && containsWhitespace(addedText)   && containsNonWhitespace(addedText))) {
		
			replaceTextRange = new ReplaceTextRange(
				replaceText.getStart(),
				replaceText.getLength(),
				addedText);
			
			System.out.println("###### update currentText");
			
			this.currentText = updatedText;
		}
		else {
			replaceTextRange = null;
		}
		
		return replaceTextRange;
	}
	
	
	private static boolean containsWhitespace(Text s) {
		for (int i = 0; i < s.length(); ++ i) {
			if (Character.isWhitespace(s.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	private static boolean containsNonWhitespace(Text s) {
		for (int i = 0; i < s.length(); ++ i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return true;
			}
		}

		return false;
	}
}
