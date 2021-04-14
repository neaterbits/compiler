package com.neaterbits.ide.core.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;

final class ActionContextsImpl implements ActionContexts {

	private final Map<Class<?>, List<ActionContext>> map;
	
	ActionContextsImpl(Collection<ActionContext> activeActionContexts) {
		
		if (activeActionContexts == null || activeActionContexts.isEmpty()) {
			map = null;
		}
		else {
			
			map = new HashMap<>(activeActionContexts.size());
			
			for (ActionContext actionContext : activeActionContexts) {
				
				final Class<? extends ActionContext> actionType = actionContext.getClass();
				
				List<ActionContext> list = map.get(actionType);
				
				if (list == null) {
					list = new ArrayList<>();
					
					map.put(actionType, list);
				}

				list.add(actionContext);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ActionContext> Collection<T> getListOfType(Class<T> cl) {
		
		Objects.requireNonNull(cl);
		
		final Collection<T> result;
		
		if (map == null) {
			result = Collections.emptyList();
		}
		else {
			@SuppressWarnings("rawtypes")
			final List<T> list = (List)map.get(cl);
			
			result = list != null ? Collections.unmodifiableCollection(list) : Collections.emptyList();
		}
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ActionContext> T getOfType(Class<T> cl) {
		final T result;
		
		if (map == null) {
			result = null;
		}
		else {
			final List<ActionContext> list = map.get(cl);
			
			if (list == null) {
				result = null;
			}
			else {
				switch (list.size()) {
				case 0:
					result = null;
					break;
					
				case 1:
					result = (T)list.get(0);
					break;
					
				default:
					throw new UnsupportedOperationException();
				}
			}
		}
		
		return result;
	}

	@Override
	public boolean hasOfType(Class<?> cl) {

		final boolean hasOfType;
		
		if (map == null) {
			hasOfType = false;
		}
		else {
			final List<ActionContext> list = map.get(cl);
			
			hasOfType = list != null ? !list.isEmpty() : false;
		}
		
		return hasOfType;
	}

	@Override
	public String toString() {
		return map != null ? map.toString() : "{ }";
	}
}
