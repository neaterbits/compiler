package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.common.ui.keys.Key;
import com.neaterbits.ide.common.ui.keys.KeyLocation;
import com.neaterbits.ide.common.ui.keys.KeyMask;

public interface KeyEventListener {

	boolean onKeyPress(Key key, KeyMask mask, KeyLocation location);
	
	boolean onKeyRelease(Key key, KeyMask mask, KeyLocation location);
}
