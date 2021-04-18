package dev.nimbler.ide.ui.actions.types.edit;

import dev.nimbler.ide.common.ui.SearchDirection;

public final class FindPreviousAction extends BaseFindAction {

	@Override
	SearchDirection getSearchDirection() {
		return SearchDirection.BACKWARD;
	}
}
