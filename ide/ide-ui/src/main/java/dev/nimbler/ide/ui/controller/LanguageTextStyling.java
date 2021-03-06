package dev.nimbler.ide.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.language.LanguageStyleOffset;
import dev.nimbler.ide.component.common.language.LanguageStyleable;
import dev.nimbler.ide.component.common.language.LanguageStyling;
import dev.nimbler.ide.ui.TextStyling;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.styling.TextColor;
import dev.nimbler.ide.util.ui.text.styling.TextStyleOffset;

final class LanguageTextStyling implements TextStyling {

	private final LanguageStyling languageStyling;
	private final TextColor defaultColor;

	LanguageTextStyling(LanguageStyling languageStyling, TextColor defaultColor) {

		Objects.requireNonNull(languageStyling);
		Objects.requireNonNull(defaultColor);
		
		this.languageStyling = languageStyling;
		this.defaultColor = defaultColor;
	}
	
	LanguageTextStyling(LanguageStyling languageStyling) {
		
		Objects.requireNonNull(languageStyling);
		
		this.languageStyling = languageStyling;
		this.defaultColor = new TextColor(0, 0, 0);
	}

	@Override
	public List<TextStyleOffset> applyStylesToLine(long lineStartPosInText, Text lineText, SourceFileModel sourceFileModel) {

		if (lineText.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		final List<TextStyleOffset> result = new ArrayList<>();
		
		long lastOffsetWithinLine = 0;
		long lastLength = 0;
		
		final Iterable<LanguageStyleOffset> offsets = languageStyling.applyStylesToLine(lineStartPosInText, lineText, sourceFileModel);
		
		for (LanguageStyleOffset offset : offsets) {

			final LanguageStyleable styleable = offset.getStyleable();

			final long afterLastInText = lineStartPosInText + lastOffsetWithinLine + lastLength;

			if (offset.getStart() > afterLastInText) {
				
				result.add(new TextStyleOffset(
						afterLastInText,
						offset.getStart() - afterLastInText,
						defaultColor));
			}
			
			result.add(new TextStyleOffset(
					offset.getStart(),
					offset.getLength(),
					styleable.getDefaultColor(),
					null,
					styleable.getTextStyles()));
			
			lastOffsetWithinLine = offset.getStart() - lineStartPosInText;
			lastLength = offset.getLength();
		}
		
		if (lineText.length() > lastOffsetWithinLine + lastLength) {
			
			result.add(new TextStyleOffset(
					lineStartPosInText + lastOffsetWithinLine + lastLength,
					lineText.length() - (lastOffsetWithinLine + lastLength),
					defaultColor));
		}
		
		return result;
	}
}
