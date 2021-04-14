package dev.nimbler.ide.core.ui.actions.types.edit;

import dev.nimbler.ide.common.ui.SearchDirection;

public final class FindNextAction extends BaseFindAction {

	@Override
	SearchDirection getSearchDirection() {
		return SearchDirection.FORWARD;
	}
}
