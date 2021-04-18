package dev.nimbler.ide.ui.view;

import dev.nimbler.ide.common.ui.keys.Key;
import dev.nimbler.ide.common.ui.keys.KeyLocation;
import dev.nimbler.ide.common.ui.keys.KeyMask;

public interface KeyEventListener {

	boolean onKeyPress(Key key, KeyMask mask, KeyLocation location);
	
	boolean onKeyRelease(Key key, KeyMask mask, KeyLocation location);
}
