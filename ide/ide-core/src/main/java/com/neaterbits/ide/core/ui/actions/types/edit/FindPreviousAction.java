package com.neaterbits.ide.core.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.SearchDirection;

public final class FindPreviousAction extends BaseFindAction {

	@Override
	SearchDirection getSearchDirection() {
		return SearchDirection.BACKWARD;
	}
}
