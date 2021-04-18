package dev.nimbler.ide.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import dev.nimbler.ide.common.ui.keys.Key;
import dev.nimbler.ide.common.ui.keys.KeyLocation;
import dev.nimbler.ide.common.ui.keys.KeyMask;
import dev.nimbler.ide.common.ui.keys.QualifierKey;
import dev.nimbler.ide.ui.view.KeyEventListener;

final class SWTKeyEventListener implements KeyListener {

	private final KeyEventListener delegate;
	
	@FunctionalInterface
	interface KeyEventHandler {
		boolean onKey(Key key, KeyMask mask, KeyLocation location);
	}

	SWTKeyEventListener(KeyEventListener delegate) {
		
		Objects.requireNonNull(delegate);
		
		this.delegate = delegate;
	}
	
	boolean keyPressed(char character, int keyCode, int keyLocation, int stateMask) {
		return convertKeyEvent(character, keyCode, keyLocation,stateMask, delegate::onKeyPress);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		event.doit = keyPressed(event.character, event.keyCode, event.keyLocation, event.stateMask);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		event.doit = convertKeyEvent(event.character, event.keyCode, event.keyLocation, event.stateMask, delegate::onKeyRelease);
	}

	private boolean convertKeyEvent(char character, int keyCode, int keyLocation, int stateMask, KeyEventHandler keyEventHandler) {
		
		final int convertedKeyCode;
		
		switch (keyCode) {
		case SWT.F1: 	convertedKeyCode = Key.F1; break;
		case SWT.F2: 	convertedKeyCode = Key.F2; break;
		case SWT.F3: 	convertedKeyCode = Key.F3; break;
		case SWT.F4: 	convertedKeyCode = Key.F4; break;
		case SWT.F5: 	convertedKeyCode = Key.F5; break;
		case SWT.F6: 	convertedKeyCode = Key.F6; break;
		case SWT.F7: 	convertedKeyCode = Key.F7; break;
		case SWT.F8: 	convertedKeyCode = Key.F8; break;
		case SWT.F9: 	convertedKeyCode = Key.F9; break;
		case SWT.F10: 	convertedKeyCode = Key.F10; break;
		case SWT.F11: 	convertedKeyCode = Key.F11; break;
		case SWT.F12: 	convertedKeyCode = Key.F12; break;
		
		default:
			convertedKeyCode = keyCode;
			break;
		}
		
		char c = Character.toLowerCase(character);
		
		final List<QualifierKey> qualifiers = new ArrayList<>();
		
		if ((stateMask & SWT.CTRL) != 0) {
			qualifiers.add(QualifierKey.CTRL);
	
			c = (char)(c + 'a' - 1);
		}

		if ((stateMask & SWT.SHIFT) != 0) {
			qualifiers.add(QualifierKey.SHIFT);
		}
		
		if ((stateMask & SWT.ALT) != 0) {
			qualifiers.add(QualifierKey.ALT);
		}

		final KeyLocation location;
		
		switch (keyLocation) {
		case SWT.LEFT:
			location = KeyLocation.LEFT;
			break;
			
		case SWT.RIGHT:
			location = KeyLocation.RIGHT;
			break;
			
		case SWT.KEYPAD:
			location = KeyLocation.KEYPAD;
			break;
			
		case SWT.NONE:
			location = KeyLocation.NONE;
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
		
		return keyEventHandler.onKey(
				new Key(c, convertedKeyCode),
				new KeyMask(qualifiers),
				location);
	}
}
