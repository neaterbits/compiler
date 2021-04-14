package com.neaterbits.ide.core.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.SearchDirection;

public final class FindNextAction extends BaseFindAction {

	@Override
	SearchDirection getSearchDirection() {
		return SearchDirection.FORWARD;
	}
}
