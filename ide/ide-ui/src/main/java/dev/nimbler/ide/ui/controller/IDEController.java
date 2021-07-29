package dev.nimbler.ide.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jutils.Strings;

import dev.nimbler.build.types.resource.NamespaceResource;
import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileHolderResourcePath;
import dev.nimbler.build.types.resource.SourceFileResource;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.config.Configuration;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.ui.actions.Action;
import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.common.ui.actions.ActionExeParameters;
import dev.nimbler.ide.common.ui.actions.ActionExecutionException;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.common.ui.keys.Key;
import dev.nimbler.ide.common.ui.keys.KeyBindings;
import dev.nimbler.ide.common.ui.keys.KeyCombination;
import dev.nimbler.ide.common.ui.keys.KeyLocation;
import dev.nimbler.ide.common.ui.keys.KeyMask;
import dev.nimbler.ide.common.ui.menus.MenuItemEntry;
import dev.nimbler.ide.common.ui.menus.Menus;
import dev.nimbler.ide.common.ui.model.ProjectsModel;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.common.ui.view.KeyEventListener;
import dev.nimbler.ide.common.ui.view.View;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.ConfigurationAccess;
import dev.nimbler.ide.component.common.IDERegisteredComponents;
import dev.nimbler.ide.ui.UI;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.keys.IDEKeyBindings;
import dev.nimbler.ide.ui.menus.IDEMenus;
import dev.nimbler.ide.ui.view.MenuSelectionListener;
import dev.nimbler.ide.ui.view.UIViewAndSubViews;
import dev.nimbler.ide.ui.view.ViewMenuItem;
import dev.nimbler.ide.component.common.ConfiguredComponent;
import dev.nimbler.ide.component.common.IDEComponent;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.component.common.ui.ComponentUI;
import dev.nimbler.ide.util.IOUtil;
import dev.nimbler.ide.util.Value;

public final class IDEController implements ComponentIDEAccess {

	private final CodeAccess codeAccess;
	
	private final ConfigurationAccess configurationAccess;

	private final IDERegisteredComponents ideComponents;
	
	private final Translator translator;
	
	private final ConfigurationManager configurationManager;
	
	private final EditUIController uiController;
	
	private final ActionExecuteState actionExecuteState;
	private final ActionApplicableParameters actionApplicableParameters;
	
	private final Map<MenuItemEntry<?, ?>, ViewMenuItem> menuMap;
	
	private final UIViewAndSubViews uiView;
	
	private View focusedView;
	
	public IDEController(
			CodeAccess codeAccess,
			UI ui,
			TextEditorConfig config,
			IDERegisteredComponents ideComponents,
			Translator uiTranslator,
			ConfigurationAccess configurationAccess) {

        Objects.requireNonNull(ideComponents);
        Objects.requireNonNull(uiTranslator);

	    Objects.requireNonNull(codeAccess);

	    Objects.requireNonNull(configurationAccess);

	    this.codeAccess = codeAccess;

		this.configurationAccess = configurationAccess;

		final ProjectsModel projectModel = new ProjectsModel(codeAccess);
		
		this.ideComponents = ideComponents;
		this.translator = uiTranslator;
		
		this.configurationManager = new ConfigurationManager(ideComponents);

		final KeyBindings keyBindings = IDEKeyBindings.makeKeyBindings();

		final Menus menus = IDEMenus.makeMenues(keyBindings, ideComponents);
		
		final UIModels uiModels = new UIModels(projectModel);
		
		final UIParameters uiParameters = new UIParameters(
		        uiTranslator,
		        keyBindings,
		        uiModels,
		        ideComponents,
		        this,
		        config);
		
		this.menuMap = new HashMap<>();
		
		final MenuSelectionListener menuListener = menuItemEntry -> {
		    
            @SuppressWarnings("unchecked")
            final MenuItemEntry<ActionAppParameters, ActionExeParameters> mie
		        = (MenuItemEntry<ActionAppParameters, ActionExeParameters>)menuItemEntry;
		    
			callMenuItemAction(mie);
		};
		
		this.uiView = ui.makeUIView(uiParameters, menus, (menuItemEntry, viewMenuItem) -> {
			menuMap.put(menuItemEntry, viewMenuItem);
			
			return menuListener;
		});
		
		this.uiController = new EditUIController(uiView, config, projectModel, ideComponents, codeAccess);
		
		final Clipboard clipboard = new ClipboardImpl(ui.getSystemClipboard());

		final UndoRedoBuffer undoRedoBuffer = new UndoRedoBuffer() {
            
            @Override
            public boolean hasUndoEntries() {
                return true;
            }
            
            @Override
            public boolean hasRedoEntries() {
                return false;
            }
        };
		
		this.actionExecuteState = new ActionExecuteState(
				ideComponents,
				uiView,
				clipboard,
				undoRedoBuffer,
				this,
				codeAccess,
				uiController,
				ui.getIOForwardToCaller()) {

		    @Override
		    SourceFileResourcePath getCurrentSourceFileResourcePath() {
		        
		        return uiController.getCurrentEditedFile();
		    }

		    @Override
		    SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath) {
		        
		        return codeAccess.getSourceFileModel(sourceFileResourcePath);
		    }
		};

		this.actionApplicableParameters
		    = new ActionApplicableParametersImpl(actionExecuteState, ideComponents.getLanguages());
		
		addKeyEventListener(uiView, keyBindings, menus);

		uiView.getViewList().addActionContextViewListener((view, updatedContexts) -> {
			updateMenuItemsEnabledState(uiView, actionApplicableParameters);
		});

		// initial update
		updateMenuItemsEnabledState(uiView, actionApplicableParameters);

		ui.addFocusListener(view -> {
			focusedView = view;

			updateMenuItemsEnabledState(uiView, actionApplicableParameters);
		});
	}

	private void addKeyEventListener(UIViewAndSubViews uiView, KeyBindings keyBindings, Menus menus) {

		uiView.addKeyEventListener(new KeyEventListener() {

			@Override
			public boolean onKeyRelease(Key key, KeyMask mask, KeyLocation location) {
				
				return true;
			}
			
			@Override
			public boolean onKeyPress(Key key, KeyMask mask, KeyLocation location) {
				
				@SuppressWarnings("unchecked")
                final Action<ActionAppParameters, ActionExeParameters> action
				    = (Action<ActionAppParameters, ActionExeParameters>)findActionWithNoKeyBindingInMenus(
				                                                    keyBindings,
				                                                    menus,
				                                                    new KeyCombination(key, mask));

				if (action != null) {
					
					if (action.isApplicableInContexts(
							actionApplicableParameters,
							getFocusedViewActionContexts(),
							getAllActionContexts(uiView))) {
					
						try {
                            action.execute(makeActionExecuteParameters());
                        } catch (ActionExecutionException ex) {
                            uiView.displayError("Caught exception from action", ex);
                        }
					}
				}
				
				return true;
			}
		});
	}

	public UIViewAndSubViews getMainView() {
        return uiView;
    }

    private static Action<?, ?> findActionWithNoKeyBindingInMenus(KeyBindings keyBindings, Menus menus, KeyCombination keyCombination) {

		Objects.requireNonNull(keyBindings);
		Objects.requireNonNull(menus);
		Objects.requireNonNull(keyCombination);
		
		final Value<Boolean> found = new Value<>();
		
		menus.iterateItems(menuItem -> {
			if (keyCombination.equals(menuItem.getKeyCombination())) {
				found.set(true);
			}
		});
		
		return found.get() != null && found.get()
				? null
				: keyBindings.findAction(keyCombination.getKey(), keyCombination.getQualifiers());
	}
	
	private ActionExecuteParameters makeActionExecuteParameters() {
		
		final ActionExecuteParameters parameters = new ActionExecuteParametersImpl(
				actionExecuteState,
				uiView.getComponentDialogContext(),
				uiView.getComponentCompositeContext(),
				focusedView,
				uiController.getCurrentEditor());
	
		return parameters;
	}
	
	private void callMenuItemAction(MenuItemEntry<ActionAppParameters, ActionExeParameters> menuItem) {
		
		Objects.requireNonNull(menuItem);
		
		try {
		    menuItem.execute(makeActionExecuteParameters());
		}
		catch (ActionExecutionException ex) {
		    uiView.displayError("Caught exception while executiong", ex);
		}
	}
	
	private void updateMenuItemsEnabledState(UIViewAndSubViews uiView, ActionApplicableParameters applicableParameters) {

		final ActionContexts focusedViewActionContexts = getFocusedViewActionContexts();
		final ActionContexts allActionContexts = getAllActionContexts(uiView);
		
		// Change menu enabled state depending on applicable contexts
		for (MenuItemEntry<?, ?> entry : menuMap.keySet()) {
		    
		    @SuppressWarnings("unchecked")
            final MenuItemEntry<ActionAppParameters, ActionExeParameters>
		        mie = (MenuItemEntry<ActionAppParameters, ActionExeParameters>)entry;
		    
			final boolean applicable = mie.isApplicableInContexts(
					applicableParameters,
					focusedViewActionContexts,
					allActionContexts);
		
			final ViewMenuItem viewMenuItem = menuMap.get(entry);
		
			viewMenuItem.setEnabled(applicable);
		}
	}
	
	private ActionContexts getFocusedViewActionContexts() {
		return new ActionContextsImpl(focusedView != null ? focusedView.getActiveActionContexts() : null);
	}

	private static ActionContexts getAllActionContexts(UIViewAndSubViews uiViews) {
		
		final List<ActionContext> actionContexts = new ArrayList<>();
		
		for (View view : uiViews.getViewList().getViews()) {
			
			final Collection<ActionContext> viewActionContexts = view.getActiveActionContexts();
			
			if (viewActionContexts != null) {
				actionContexts.addAll(viewActionContexts);
			}
		}
		
		return new ActionContextsImpl(actionContexts);
	}
	
	@Override
	public void writeAndOpenFile(
			String projectName,
			String sourceFolder,
			String [] namespacePath,
			String fileName,
			String text) throws IOException {

		final SourceFolderResourcePath folder = findSourceFolder(projectName, sourceFolder);
		
		final SourceFileHolderResourcePath sourceFileHolderPath = namespacePath != null
				? new NamespaceResourcePath(
						folder,
						new NamespaceResource(
								new File(folder.getFile(), Strings.join(namespacePath, File.separatorChar)),
								namespacePath))
				: folder;
		
		final File sourceDirectory = sourceFileHolderPath.getFile();
		
		if (!sourceDirectory.exists()) {
			if (!sourceDirectory.mkdirs()) {
				throw new IOException("Failed to create directory " + sourceDirectory.getPath());
			}
		}
		else {
			if (!sourceDirectory.isDirectory()) {
				throw new IOException("Source directory exists but is not a directory");
			}
		}
						
		final File file = new File(sourceDirectory, fileName);
						
		if (file.exists()) {
			throw new IOException("Source file already exists: " + file.getPath());
		}
		
		IOUtil.writeAll(file, text);
		
		final SourceFileResourcePath sourceFile = new SourceFileResourcePath(
				sourceFileHolderPath,
				new SourceFileResource(file));
		
		uiController.openSourceFileForEditing(sourceFile);
		
		uiController.showInProjectView(sourceFile, false);
	}

	@Override
    public <T extends Configuration> T getConfiguration(Class<T> type) {

	    return configurationAccess.getConfiguration(type);
    }

    public Languages getLanguages() {
        return actionExecuteState.getComponents().getLanguages();
    }

    /*
    @Override
	public File getRootPath() {
		return buildRoot.getPath();
	}
    */

	@Override
    public List<ProjectModuleResourcePath> getRootModules() {
	    
	    return actionExecuteState.getCodeAccess().getRootModules();
    }

    @Override
	public boolean isValidSourceFolder(String projectName, String sourceFolder) {

		final SourceFolderResourcePath folder = codeAccess.findSourceFolder(projectName, sourceFolder);
		
		return folder != null;
	}

	private SourceFolderResourcePath findSourceFolder(String projectName, String sourceFolder) {

		return codeAccess.findSourceFolder(projectName, sourceFolder);
	}

    @Override
    public Translator getTranslator() {
        return translator;
    }

    @Override
    public <T> T readConfigurationFile(
            Class<? extends ConfiguredComponent> componentType,
            Class<T> configurationType,
            ProjectModuleResourcePath module) throws IOException {

        return configurationManager.readComponentConfiguration(componentType, configurationType, module);
    }

    @Override
    public void saveConfigurationFile(
            Class<? extends ConfiguredComponent> componentType, 
            Object configuration,
            ProjectModuleResourcePath module) throws IOException {

        configurationManager.saveComponentConfiguration(componentType, configuration, module);
    }

    @Override
    public void displayError(String title, Exception ex) {
        uiView.displayError(title, ex);
    }

    @Override
    public <T extends IDEComponent> List<T> findComponents(Class<T> type) {
        return ideComponents.findComponents(type);
    }

    @Override
    public <T extends ComponentUI> List<T> findComponentUIs(Class<T> type) {
        return ideComponents.findComponentUIs(type);
    }
}
