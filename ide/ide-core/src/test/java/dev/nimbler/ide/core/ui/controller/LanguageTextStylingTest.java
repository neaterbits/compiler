package dev.nimbler.ide.core.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.language.LanguageStyleOffset;
import dev.nimbler.ide.component.common.language.LanguageStyleable;
import dev.nimbler.ide.component.common.language.LanguageStyling;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.styling.TextColor;
import dev.nimbler.ide.util.ui.text.styling.TextStyleOffset;

public class LanguageTextStylingTest {

	@Test
	public void testStylingAtStart() {

		final LanguageStyling styling = Mockito.mock(LanguageStyling.class);
		final SourceFileModel sourceFileModel = Mockito.mock(SourceFileModel.class);
		
		final TextColor textColor = new TextColor(0, 0, 0);
		
		final LanguageTextStyling languageTextStyling = new LanguageTextStyling(styling, textColor);
		
		final long styleStart = 0L;
		final long styleLength = 4L;
		
		final Collection<LanguageStyleOffset> offsets = Arrays.asList(
				new LanguageStyleOffset(styleStart, styleLength, LanguageStyleable.KEYWORD_DEFAULT)
		);
		
		when(styling.applyStylesToLine(anyLong(), any(Text.class), same(sourceFileModel)))
			.thenReturn(offsets);

		List<TextStyleOffset> textStyleOffsets = languageTextStyling.applyStylesToLine(
				0L,
				new StringText("test text"),
				sourceFileModel);
		
		assertThat(textStyleOffsets.size()).isEqualTo(2);
		
		assertThat(textStyleOffsets.get(0).getColor()).isNotNull();
		assertThat(textStyleOffsets.get(0).getStart()).isEqualTo(styleStart);
		assertThat(textStyleOffsets.get(0).getLength()).isEqualTo(styleLength);
		assertThat(textStyleOffsets.get(0).getColor()).isSameAs(LanguageStyleable.KEYWORD_DEFAULT.getDefaultColor());

		assertThat(textStyleOffsets.get(1).getStart()).isEqualTo(4);
		assertThat(textStyleOffsets.get(1).getLength()).isEqualTo(5);
		assertThat(textStyleOffsets.get(1).getColor()).isSameAs(textColor);
	}

	@Test
	public void testStylingAtMiddle() {

		final LanguageStyling styling = Mockito.mock(LanguageStyling.class);
		final SourceFileModel sourceFileModel = Mockito.mock(SourceFileModel.class);
		
		final TextColor textColor = new TextColor(0, 0, 0);
		
		final LanguageTextStyling languageTextStyling = new LanguageTextStyling(styling, textColor);
		
		final long styleStart = 5L;
		final long styleLength = 3L;
		
		final Collection<LanguageStyleOffset> offsets = Arrays.asList(
				new LanguageStyleOffset(styleStart, styleLength, LanguageStyleable.KEYWORD_DEFAULT)
		);
		
		when(styling.applyStylesToLine(anyLong(), any(Text.class), same(sourceFileModel)))
			.thenReturn(offsets);

		List<TextStyleOffset> textStyleOffsets = languageTextStyling.applyStylesToLine(
				0L,
				new StringText("test mid text"),
				sourceFileModel);
		
		assertThat(textStyleOffsets.size()).isEqualTo(3);

		assertThat(textStyleOffsets.get(0).getStart()).isEqualTo(0);
		assertThat(textStyleOffsets.get(0).getLength()).isEqualTo(5);
		assertThat(textStyleOffsets.get(0).getColor()).isSameAs(textColor);

		assertThat(textStyleOffsets.get(1).getStart()).isEqualTo(styleStart);
		assertThat(textStyleOffsets.get(1).getLength()).isEqualTo(styleLength);
		assertThat(textStyleOffsets.get(1).getColor()).isSameAs(LanguageStyleable.KEYWORD_DEFAULT.getDefaultColor());

		assertThat(textStyleOffsets.get(2).getStart()).isEqualTo(8);
		assertThat(textStyleOffsets.get(2).getLength()).isEqualTo(5);
		assertThat(textStyleOffsets.get(2).getColor()).isSameAs(textColor);
	}

	@Test
	public void testStylingManyAtMiddle() {
		checkStylingManyAtMiddle(0L);
		checkStylingManyAtMiddle(1L);
		checkStylingManyAtMiddle(120L);
	}
		
	private void checkStylingManyAtMiddle(long startPos) {

		final LanguageStyling styling = Mockito.mock(LanguageStyling.class);
		final SourceFileModel sourceFileModel = Mockito.mock(SourceFileModel.class);
		
		final TextColor textColor = new TextColor(0, 0, 0);
		
		final LanguageTextStyling languageTextStyling = new LanguageTextStyling(styling, textColor);
		
		final Collection<LanguageStyleOffset> offsets = Arrays.asList(
				new LanguageStyleOffset(startPos + 5, 3, LanguageStyleable.KEYWORD_DEFAULT),
				new LanguageStyleOffset(startPos + 9, 5, LanguageStyleable.KEYWORD_DEFAULT)
		);
		
		when(styling.applyStylesToLine(anyLong(), any(Text.class), same(sourceFileModel)))
			.thenReturn(offsets);

		
		List<TextStyleOffset> textStyleOffsets = languageTextStyling.applyStylesToLine(
				startPos,
				new StringText("test mid words text"),
				sourceFileModel);
		
		assertThat(textStyleOffsets.size()).isEqualTo(5);

		assertThat(textStyleOffsets.get(0).getStart()).isEqualTo(startPos + 0);
		assertThat(textStyleOffsets.get(0).getLength()).isEqualTo(5);
		assertThat(textStyleOffsets.get(0).getColor()).isSameAs(textColor);

		assertThat(textStyleOffsets.get(1).getStart()).isEqualTo(startPos + 5);
		assertThat(textStyleOffsets.get(1).getLength()).isEqualTo(3);
		assertThat(textStyleOffsets.get(1).getColor()).isSameAs(LanguageStyleable.KEYWORD_DEFAULT.getDefaultColor());

		assertThat(textStyleOffsets.get(2).getStart()).isEqualTo(startPos + 8);
		assertThat(textStyleOffsets.get(2).getLength()).isEqualTo(1);
		assertThat(textStyleOffsets.get(2).getColor()).isSameAs(textColor);
		
		assertThat(textStyleOffsets.get(3).getStart()).isEqualTo(startPos + 9);
		assertThat(textStyleOffsets.get(3).getLength()).isEqualTo(5);
		assertThat(textStyleOffsets.get(3).getColor()).isSameAs(LanguageStyleable.KEYWORD_DEFAULT.getDefaultColor());

		assertThat(textStyleOffsets.get(4).getStart()).isEqualTo(startPos + 14);
		assertThat(textStyleOffsets.get(4).getLength()).isEqualTo(5);
		assertThat(textStyleOffsets.get(4).getColor()).isSameAs(textColor);
	}

	@Test
	public void testStylingAtEnd() {
		
		checkStylingAtEnd(0L);
		checkStylingAtEnd(1L);
	}
	
	private void checkStylingAtEnd(long startPos) {

		final LanguageStyling styling = Mockito.mock(LanguageStyling.class);
		final SourceFileModel sourceFileModel = Mockito.mock(SourceFileModel.class);
		
		final TextColor textColor = new TextColor(0, 0, 0);
		
		final LanguageTextStyling languageTextStyling = new LanguageTextStyling(styling, textColor);
		
		
		final Collection<LanguageStyleOffset> offsets = Arrays.asList(
				new LanguageStyleOffset(startPos + 5L, 4L, LanguageStyleable.KEYWORD_DEFAULT)
		);
		
		when(styling.applyStylesToLine(anyLong(), any(Text.class), same(sourceFileModel)))
			.thenReturn(offsets);

		List<TextStyleOffset> textStyleOffsets = languageTextStyling.applyStylesToLine(
				startPos,
				new StringText("test text"),
				sourceFileModel);
		
		assertThat(textStyleOffsets.size()).isEqualTo(2);
		
		assertThat(textStyleOffsets.get(0).getStart()).isEqualTo(startPos + 0L);
		assertThat(textStyleOffsets.get(0).getLength()).isEqualTo(5L);
		assertThat(textStyleOffsets.get(0).getColor()).isSameAs(textColor);

		assertThat(textStyleOffsets.get(1).getStart()).isEqualTo(startPos + 5L);
		assertThat(textStyleOffsets.get(1).getLength()).isEqualTo(4L);
		assertThat(textStyleOffsets.get(1).getColor()).isSameAs(LanguageStyleable.KEYWORD_DEFAULT.getDefaultColor());
	}
}

