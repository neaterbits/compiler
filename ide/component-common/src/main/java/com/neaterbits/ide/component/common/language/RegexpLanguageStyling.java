package com.neaterbits.ide.component.common.language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.util.ui.text.Text;

public class RegexpLanguageStyling implements LanguageStyling {

	private final List<LanguageStyleable> styleables;
	
	protected RegexpLanguageStyling(Collection<RegexpLanguageStyleable> styleables) {

		this.styleables = new ArrayList<>(styleables);
	}
	
	@Override
	public final Iterable<LanguageStyleable> getStylables() {
		return styleables;
	}

	@Override
	public Iterable<LanguageStyleOffset> applyStylesToLine(long lineOffset, Text text, SourceFileModel sourceFileModel) {
		
		final List<LanguageStyleOffset> styleOffsets = new ArrayList<>();

		for (LanguageStyleable styleable : styleables) {

			final RegexpLanguageStyleable regexpStyleable = (RegexpLanguageStyleable)styleable;
			
			final Matcher matcher = regexpStyleable.getPattern().matcher(text.asString());
			
			while (matcher.find()) {
				final int start = matcher.start();
				final int end = matcher.end();
				
				styleOffsets.add(new LanguageStyleOffset(start, end - start, regexpStyleable));
			}
		}

		return styleOffsets;
	}
}
