package com.neaterbits.ide.common.ui.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ViewList {

	private final List<ActionContextViewListener> actionContextViewListeners;
	
	private static class ViewEntry {
		private final View view;
		private final View impl;

		ViewEntry(View view, View impl) {
			this.view = view;
			this.impl = impl;
		}
	}
	
	private final List<ViewEntry> views;
	
	
	protected ViewList() {
		this.views = new ArrayList<>();
		
		this.actionContextViewListeners = new ArrayList<>();
	}
	
	public Collection<View> getViews() {
		return Collections.unmodifiableCollection(views.stream().map(view -> view.view).collect(Collectors.toList()));
	}

	public void addActionContextViewListener(ActionContextViewListener listener) {
		
		Objects.requireNonNull(listener);
		
		if (actionContextViewListeners.contains(listener)) {
			throw new IllegalArgumentException();
		}
		
		actionContextViewListeners.add(listener);
	}
	
	protected final View addView(View view) {
		
		Objects.requireNonNull(view);
		
		if (views.stream().anyMatch(entry -> entry.impl == view)) {
			throw new IllegalStateException();
		}
		
		views.add(new ViewEntry(view, view));
		
		view.addActionContextListener(actionContexts -> {
			for (ActionContextViewListener listener : actionContextViewListeners) {
				listener.onUpdated(view, actionContexts);
			}
		});
		
		return view;
	}
	
	protected final void removeView(View view) {
		
		Objects.requireNonNull(view);
		
		final Iterator<ViewEntry> iter = views.iterator();
		
		boolean removed = false;
		
		while (iter.hasNext()) {
			
			if (iter.next().impl == view) {
				iter.remove();
				
				removed = true;
				break;
			}
		}
		
		if (!removed) {
			throw new IllegalStateException();
		}
	}
}
