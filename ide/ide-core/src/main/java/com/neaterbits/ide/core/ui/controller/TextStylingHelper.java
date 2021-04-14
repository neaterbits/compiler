package com.neaterbits.ide.core.ui.controller;

import java.util.Collection;
import java.util.Collections;

import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.core.ui.TextStyling;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

class TextStylingHelper {

	static TextStylingModel makeTextStylingModel(LanguageComponent languageComponent, SourceFileModel sourceFileModel) {
		
		final TextStyling textStyling = makeTextStyling(languageComponent);
		
		return new TextStylingModel() {
			
			@Override
			public Collection<TextStyleOffset> getLineStyleOffsets(long startPos, long length, Text lineText) {

				return makeStylesForLine(sourceFileModel, startPos, length, lineText, textStyling);
			}
		};
	}
	
	private static TextStyling makeTextStyling(LanguageComponent languageComponent) {
		
		TextStyling textStyling = null;
		
		if (languageComponent != null) {
			
			if (languageComponent != null && languageComponent.getStyling() != null) {

				textStyling = new LanguageTextStyling(languageComponent.getStyling());
				
			}
		}
		
		return textStyling;
	}
	
	
	private static Collection<TextStyleOffset> makeStylesForLine(
			SourceFileModel sourceFileModel,
			long lineStartOffset, long lineLength, Text lineText,
			TextStyling textStyling) {

		if (lineStartOffset < 0) {
			throw new IllegalArgumentException();
		}
		
		final Collection<TextStyleOffset> styleOffsets;
		
		if (lineLength == 0) {
			styleOffsets = Collections.emptyList();
		}
		else {
			styleOffsets = textStyling.applyStylesToLine(lineStartOffset, lineText, sourceFileModel);
		}
		
		return styleOffsets;
	}
}
