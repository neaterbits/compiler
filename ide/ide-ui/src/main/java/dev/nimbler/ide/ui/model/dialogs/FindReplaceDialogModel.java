package dev.nimbler.ide.ui.model.dialogs;

import java.util.Objects;

import dev.nimbler.ide.common.ui.SearchDirection;
import dev.nimbler.ide.common.ui.SearchScope;

public final class FindReplaceDialogModel {

	public static final FindReplaceDialogModel INITIAL = new FindReplaceDialogModel(
			"",
			"",
			SearchDirection.FORWARD,
			SearchScope.ALL,
			false, true, false);
	
	
	private final String searchFor;
	private final String replaceWith;
	
	private final SearchDirection direction;
	private final SearchScope scope;
	
	private final boolean caseSensitive;
	private final boolean wrap;
	private final boolean wholeWord;
	
	public FindReplaceDialogModel(
			String searchFor,
			String replaceWith,
			SearchDirection direction,
			SearchScope scope,
			boolean caseSensitive, boolean wrap, boolean wholeWord) {

		Objects.requireNonNull(searchFor);
		Objects.requireNonNull(replaceWith);
		Objects.requireNonNull(direction);
		Objects.requireNonNull(scope);
		
		this.searchFor = searchFor;
		this.replaceWith = replaceWith;

		this.direction = direction;
		this.scope = scope;
		this.caseSensitive = caseSensitive;
		this.wrap = wrap;
		this.wholeWord = wholeWord;
	}

	public boolean hasSearchText() {
		return !searchFor.isEmpty();
	}

	public String getSearchFor() {
		return searchFor;
	}

	public String getReplaceWith() {
		return replaceWith;
	}

	public SearchDirection getDirection() {
		return direction;
	}

	public SearchScope getScope() {
		return scope;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public boolean isWrap() {
		return wrap;
	}

	public boolean isWholeWord() {
		return wholeWord;
	}
}
