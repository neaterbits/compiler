package dev.nimbler.ide.swt;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.ide.common.ui.ViewFocusListener;
import dev.nimbler.ide.common.ui.menus.Menus;
import dev.nimbler.ide.common.ui.view.View;
import dev.nimbler.ide.ui.UI;
import dev.nimbler.ide.ui.controller.UIParameters;
import dev.nimbler.ide.ui.swt.SWTViewList;
import dev.nimbler.ide.ui.view.MapMenuItem;
import dev.nimbler.ide.ui.view.SystemClipboard;
import dev.nimbler.ide.ui.view.UIViewAndSubViews;

public class SWTUI implements UI {

	private final Display display;
	
	private final SWTSystemClipboard systemClipboard;
	
	public SWTUI(Display display) {
	
		Objects.requireNonNull(display);
		
		this.display = display;

		this.systemClipboard = new SWTSystemClipboard(display);
	}
	
	@Override
	public ForwardResultToCaller getIOForwardToCaller() {
		return new ForwardResultToCaller() {
			
			@Override
			public void forward(Runnable runnable) {
				display.asyncExec(runnable);
			}
		};
	}
	
	@Override
	public SystemClipboard getSystemClipboard() {
		return systemClipboard;
	}

	@Override
	public UIViewAndSubViews makeUIView(UIParameters uiParameters, Menus menus, MapMenuItem mapMenuItem) {
	
		return new SWTUIView(display, uiParameters, menus, mapMenuItem);
	}

	private View focusedView = null;
	
	@Override
	public void addFocusListener(ViewFocusListener focusListener) {
		display.addFilter(SWT.FocusIn, new Listener() {
			
			@Override
			public void handleEvent(Event event) {

				focusedView = SWTViewList.findView(event.widget);
				
				if (focusedView != null) {
					focusListener.onViewFocusChange(focusedView);
				}
			}
		});

		display.addFilter(SWT.FocusOut, new Listener() {
			
			@Override
			public void handleEvent(Event event) {

				final View view = SWTViewList.findView(event.widget);
				
				if (view != null) {
					
					if (view != focusedView) {
						throw new IllegalStateException();
					}
					
					focusedView = null;

					focusListener.onViewFocusChange(null);
				}
			}
		});
	}

	
	@Override
	public void runInitialEvents() {
	    
		while (display.readAndDispatch())
			;
	}

	@Override
	public void main(UIViewAndSubViews mainView) {

	    while (!((SWTUIView)mainView).isClosed()) {
	        
    		if (!display.readAndDispatch()) {
    			display.sleep();
    		}
	    }
	}
}
