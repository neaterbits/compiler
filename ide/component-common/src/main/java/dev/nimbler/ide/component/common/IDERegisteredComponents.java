package dev.nimbler.ide.component.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.types.LanguageName;
import dev.nimbler.ide.common.tasks.TasksListener;
import dev.nimbler.ide.common.ui.controller.EditorsListener;
import dev.nimbler.ide.component.common.instantiation.InstantiationComponent;
import dev.nimbler.ide.component.common.instantiation.InstantiationComponentUI;
import dev.nimbler.ide.component.common.instantiation.Newable;
import dev.nimbler.ide.component.common.instantiation.NewableCategory;
import dev.nimbler.ide.component.common.instantiation.NewableCategoryName;
import dev.nimbler.ide.component.common.language.LanguageComponent;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.component.common.ui.ComponentUI;
import dev.nimbler.ide.component.common.ui.DetailsComponentUI;

public final class IDERegisteredComponents implements IDEComponentsConstAccess {

	private final List<IDERegisteredComponent> components;

	private final Map<LanguageName, LanguageComponent> languageComponents;
	
	private final Languages languages;
	
	public IDERegisteredComponents() {
		this.components = new ArrayList<>();
	
		this.languageComponents = new HashMap<>();
		
		this.languages = new Languages() {
			@Override
			public LanguageComponent getLanguageComponent(LanguageName languageName) {
				Objects.requireNonNull(languageName);

				return languageComponents.get(languageName);
			}

			@Override
			public LanguageName detectLanguage(SourceFileResourcePath sourceFile) {
				
				final String fileName = sourceFile.getName();
				
				final int suffixIndex = fileName.lastIndexOf('.');
				
				if (suffixIndex >= 0 && suffixIndex < fileName.length() - 1) {
				
					final String suffix = fileName.substring(suffixIndex + 1);
					
					for (LanguageComponent language : languageComponents.values()) {
						for (String languageSuffix : language.getFileSuffixes()) {
							if (suffix.toLowerCase().equals(languageSuffix.toLowerCase())) {
								return language.getLanguageName();
							}
						}
					}
				}
				
				return null;
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> getUIListeners(Class<T> type) {
		
	    return components.stream()
	            .map(c -> c.getComponentUI())
	            .filter(c -> type.isAssignableFrom(c.getClass()))
	            .map(c -> (T)c)
	            .collect(Collectors.toList());
	}
	
	public List<EditorsListener> getEditorsListeners() {

	    return getUIListeners(EditorsListener.class);
	}
	
	public List<TasksListener> getTasksListeners() {
		
		return getUIListeners(TasksListener.class);
	}

	@Override
    public List<DetailsComponentUI<?>> getDetailsComponentUIs() {
        return components.stream()
                .map(registered -> registered.getComponentUI())
                .filter(c -> c instanceof DetailsComponentUI<?>)
                .map(c -> (DetailsComponentUI<?>)c)
                .collect(Collectors.toList());
    }

    @Override
	public Languages getLanguages() {
		return languages;
	}
	
	public void registerComponent(IDEComponent component, ComponentUI componentUI) {

		if (component instanceof LanguageComponent) {
			final LanguageComponent languageComponent = (LanguageComponent)component;
		
			languageComponents.put(languageComponent.getLanguageName(), languageComponent);
		}
		
		components.add(new IDERegisteredComponent(component, componentUI));
	}
	
	@Override
	public List<NewableCategory> getNewableCategories() {
		
		final Map<NewableCategory, Set<Newable>> map = new HashMap<>();
		
		for (IDERegisteredComponent ideComponent : components) {
			
		    if (ideComponent.getComponent() instanceof InstantiationComponent) {
		        
		        final InstantiationComponent instantiationComponent
		            = (InstantiationComponent)ideComponent.getComponent();
		        
    			final List<NewableCategory> componentNewableCategories = instantiationComponent.getNewables();
    			
    			if (componentNewableCategories != null) {
    				for (NewableCategory newableCategory : componentNewableCategories) {
    	
    					if (newableCategory.getTypes() == null || newableCategory.getTypes().isEmpty()) {
    						throw new IllegalStateException();
    					}
    					
    					Set<Newable> newables = map.get(newableCategory);
    					
    					if (newables == null) {
    						newables = new HashSet<>();
    						
    						map.put(newableCategory, newables);
    					}
    					
    					for (Newable newable : newableCategory.getTypes()) {
    						newables.add(newable);
    					}
    				}
    			}
			}
		}
		
		final List<NewableCategory> categories = new ArrayList<>(map.size());

		for (Map.Entry<NewableCategory, Set<Newable>> entry : map.entrySet()) {
			
			final NewableCategory category = new NewableCategory(
					entry.getKey().getName(),
					entry.getKey().getDisplayName(),
					new ArrayList<>(entry.getValue()));
			
			Collections.sort(
					category.getTypes(),
					(newable1, newable2) -> newable1.getDisplayName().compareTo(newable2.getDisplayName()));
			
			categories.add(category);
		}
		
		Collections.sort(
				categories,
				(category1, category2) -> category1.getDisplayName().compareTo(category2.getDisplayName()));
		
		return Collections.unmodifiableList(categories);
	}
	
	@Override
	public InstantiationComponentUI findInstantiationUIComponent(NewableCategoryName category, Newable newable) {
		
		Objects.requireNonNull(category);
		Objects.requireNonNull(newable);
		
		for (IDERegisteredComponent ideComponent : components) {
		    
		    if (ideComponent.getComponent() instanceof InstantiationComponent) {
		        
		        final InstantiationComponent instantiationComponent
		            = (InstantiationComponent)ideComponent.getComponent();
			
    			if (instantiationComponent.getNewables() != null) {
    				for (NewableCategory componentCategory : instantiationComponent.getNewables()) {
    					if (category.getName().equals(componentCategory.getName())) {
    						for (Newable componentNewable : componentCategory.getTypes()) {
    							if (newable.equals(componentNewable)) {
    								return (InstantiationComponentUI)ideComponent.getComponentUI();
    							}
    						}
    					}
    				}
    			}
		    }
		}

		return null;
	}
}
