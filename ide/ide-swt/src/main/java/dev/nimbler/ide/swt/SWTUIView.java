package dev.nimbler.ide.swt;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.ide.common.ui.keys.KeyCombination;
import dev.nimbler.ide.common.ui.keys.KeyMask;
import dev.nimbler.ide.common.ui.keys.QualifierKey;
import dev.nimbler.ide.common.ui.menus.MenuEntry;
import dev.nimbler.ide.common.ui.menus.MenuItemEntry;
import dev.nimbler.ide.common.ui.menus.MenuListEntry;
import dev.nimbler.ide.common.ui.menus.Menus;
import dev.nimbler.ide.common.ui.menus.SeparatorMenuEntry;
import dev.nimbler.ide.common.ui.menus.SubMenuEntry;
import dev.nimbler.ide.common.ui.menus.TextMenuEntry;
import dev.nimbler.ide.common.ui.translation.Translateable;
import dev.nimbler.ide.common.ui.translation.Translator;
import dev.nimbler.ide.common.ui.view.KeyEventListener;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.instantiation.InstantiationComponentUI;
import dev.nimbler.ide.component.common.instantiation.Newable;
import dev.nimbler.ide.component.common.instantiation.NewableCategory;
import dev.nimbler.ide.component.common.instantiation.NewableCategoryName;
import dev.nimbler.ide.component.common.ui.DetailsComponentUI;
import dev.nimbler.ide.ui.controller.UIParameters;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.model.dialogs.NewableSelection;
import dev.nimbler.ide.ui.model.dialogs.OpenTypeDialogModel;
import dev.nimbler.ide.ui.swt.SWTCompositeUIContext;
import dev.nimbler.ide.ui.swt.SWTDialogUIContext;
import dev.nimbler.ide.ui.swt.SWTKeyEventListener;
import dev.nimbler.ide.ui.swt.SWTViewList;
import dev.nimbler.ide.ui.view.EditorsView;
import dev.nimbler.ide.ui.view.MapMenuItem;
import dev.nimbler.ide.ui.view.MenuSelectionListener;
import dev.nimbler.ide.ui.view.ProjectView;
import dev.nimbler.ide.ui.view.UIViewAndSubViews;
import dev.nimbler.ide.ui.view.ViewMenuItem;
import dev.nimbler.ide.ui.view.dialogs.FindReplaceDialog;

public final class SWTUIView implements UIViewAndSubViews {

	private final SWTViewList viewList;
	private final Display display;
	private final UIParameters uiParameters;
	
	private final Shell window;
	
	private final Composite composite;
	
	// private final Composite overviewComposite;
	private final TabFolder overviewTabFolder;
	private final GridData overviewGridData;
	private final GridData detailsGridData;
	private final TabFolder detailsTabFolder;
	
	private final SWTProjectView projectView;
	private final SWTEditorsView editorsView;
	
	private final SWTDialogUIContext dialogUIContext;
	private final SWTCompositeUIContext compositeUIContext;
	
	private boolean editorsMaximized;
	
	SWTUIView(Display display, UIParameters uiParameters, Menus menus, MapMenuItem mapMenuItem) {
		
		this.viewList = new SWTViewList();
		
		this.display = display;
		this.uiParameters = uiParameters;
		
		this.window = new Shell(display);
		
		window.setLocation(350, 50);
		window.setSize(1280, 1024);
		
		window.setLayout(new FillLayout());
		
		final Menu menu = buildMenus(window, menus, mapMenuItem, uiParameters.getTranslator());
		
		window.setMenuBar(menu);
		
		this.composite = new Composite(window, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));

		this.overviewTabFolder = new TabFolder(composite, SWT.NONE);

		// this.overviewComposite = new Composite(composite, SWT.NONE);
		
		this.overviewGridData = new GridData();
		
		/*
		overviewGridData.minimumHeight = 600;
		*/

		overviewGridData.minimumWidth = 500;
		overviewGridData.widthHint = 300;
		
		overviewGridData.horizontalSpan = 1;
		// overviewGridData.grabExcessHorizontalSpace = true;
		overviewGridData.horizontalAlignment = SWT.FILL;
		overviewGridData.verticalSpan = 4;
		overviewGridData.grabExcessVerticalSpace = true;
		overviewGridData.verticalAlignment = SWT.FILL;

		overviewTabFolder.setLayoutData(overviewGridData);
		
		// overviewComposite.setLayoutData(overviewGridData);
		// overviewComposite.setLayout(new FillLayout());
		
		this.projectView = new SWTProjectView(
				viewList,
				overviewTabFolder,
				uiParameters.getUIModels().getProjectsModel());
		
		final GridData editorsGridData = new GridData();
		
		editorsGridData.grabExcessHorizontalSpace = true;
		editorsGridData.horizontalSpan = 3;
		editorsGridData.verticalSpan = 3;
		editorsGridData.grabExcessVerticalSpace = true;
		editorsGridData.horizontalAlignment = SWT.FILL;
		editorsGridData.grabExcessVerticalSpace = true;
		editorsGridData.verticalAlignment = SWT.FILL;
		
		this.editorsView = new SWTEditorsView(viewList, composite, uiParameters.getTextEditorConfig());
		
		editorsView.getTabFolder().setLayoutData(editorsGridData);

		this.detailsGridData = new GridData();

		// detailsGridData.grabExcessHorizontalSpace = true;
		detailsGridData.horizontalSpan = 3;
		detailsGridData.horizontalAlignment = SWT.FILL;
		detailsGridData.verticalSpan = 1;
		detailsGridData.heightHint = 350;
		
		this.detailsTabFolder = new TabFolder(composite, SWT.NONE);
		
		detailsTabFolder.setLayoutData(detailsGridData);
		
		this.compositeUIContext = new SWTCompositeUIContext(viewList, detailsTabFolder);
		
		for (DetailsComponentUI<?> detailsComponentUI : uiParameters.getComponentsAccess().getDetailsComponentUIs()) {
		    
		    final Control control
		        = (Control)detailsComponentUI.addCompositeComponentUI(
		                                            compositeUIContext,
		                                            uiParameters.getComponentIDEAccess());
		    
		    final TabItem tabItem = new TabItem(detailsTabFolder, SWT.NONE);
		    
		    final Translateable translateable
		        = Translateable.fromComponent(
		                DetailsComponentUI.TITLE_TRANSLATION_ID,
		                detailsComponentUI.getClass());

		    final String translation = uiParameters.getTranslator().translate(translateable);
		    
		    if (translation != null) {
		        tabItem.setText(translation);
		    }
		    
		    tabItem.setControl(control);
		}

		window.addDisposeListener(event -> System.exit(0));
		
		// window.pack();

		this.dialogUIContext = new SWTDialogUIContext(window);

		window.open();
	
		this.editorsMaximized = false;
	}
	
	@Override
	public SWTViewList getViewList() {
		return viewList;
	}

	private static Menu buildMenus(Shell shell, Menus menus, MapMenuItem mapMenuItems, Translator translator) {
	
		final Menu rootMenu = new Menu(shell, SWT.BAR);
		
		buildMenuList(shell, rootMenu, menus, mapMenuItems, translator);
		
		return rootMenu;
	}
	
	private static void buildMenuList(Shell shell, Menu menu, MenuListEntry list, MapMenuItem mapMenuItems, Translator translator) {

		final StringBuilder sb = new StringBuilder();

		for (MenuEntry entry : list.getEntries()) {
			
			final MenuItem menuItem;

			final TextMenuEntry textMenuEntry;
			
			sb.setLength(0);
			
			if (entry instanceof SubMenuEntry) {
				
				menuItem = new MenuItem(menu, SWT.CASCADE);
				
				final Menu subMenu = new Menu(shell, SWT.DROP_DOWN);
				menuItem.setMenu(subMenu);
				
				buildMenuList(shell, subMenu, (SubMenuEntry)entry, mapMenuItems, translator);
				
				textMenuEntry = (TextMenuEntry)entry;
			}
			else if (entry instanceof SeparatorMenuEntry){
				menuItem = new MenuItem(menu, SWT.SEPARATOR);
				
				textMenuEntry = null;
			}
			else {
				menuItem = new MenuItem(menu, SWT.PUSH);
				
				final ViewMenuItem viewMenuItem = new ViewMenuItem() {
					@Override
					public void setEnabled(boolean enabled) {
						menuItem.setEnabled(enabled);
					}
				};


				final MenuItemEntry<?, ?> menuItemEntry = (MenuItemEntry<?, ?>)entry;
				
				textMenuEntry = menuItemEntry;
				
				final KeyCombination keyCombination = menuItemEntry.getKeyCombination();

				if (keyCombination != null) {
							
					final int accelerator = applyKeyShortcut(keyCombination, sb);

					menuItem.setAccelerator(accelerator);
				}
				
				final MenuSelectionListener listener = mapMenuItems.apply((MenuItemEntry<?, ?>)entry, viewMenuItem);
				
				menuItem.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent event) {
						listener.onMenuItemSelected(menuItemEntry);
					}
				});
			}

			if (textMenuEntry != null) {
				final String text = sb.length() == 0
						? translator.translate(textMenuEntry)
						: translator.translate(textMenuEntry) + '\t' + sb.toString();
				
				menuItem.setText(text);
			}
		}
	}
	
	private static int applyKeyShortcut(KeyCombination keyCombination, StringBuilder sb) {
		
		final char keyCharacter = Character.toUpperCase(keyCombination.getKey().getCharacter());
		
		int accelerator = keyCharacter;
		
		final KeyMask qualifiers = keyCombination.getQualifiers();

		if (qualifiers.isSet(QualifierKey.SHIFT)) {
			accelerator |= SWT.SHIFT;
			
			appendQualifier(sb, "Shift");
		}

		if (qualifiers.isSet(QualifierKey.CTRL)) {
			accelerator |= SWT.CONTROL;
			
			appendQualifier(sb, "Ctrl");
		}

		if (qualifiers.isSet(QualifierKey.ALT)) {
			accelerator |= SWT.ALT;

			appendQualifier(sb, "Alt");
		}
		
		if (qualifiers.isSet(QualifierKey.ALT_GR)) {
			accelerator |= SWT.ALT_GR;

			appendQualifier(sb, "Alt Gr");
		}
		
		appendQualifier(sb, String.valueOf(keyCharacter));
		
		return accelerator;
	}
	
	private static void appendQualifier(StringBuilder sb, String qualifier) {
		if (sb.length() > 0) {
			sb.append('+');
		}
		
		sb.append(qualifier);
	}
	
	@Override
	public void setWindowTitle(String title) {
		window.setText(title);
	}

	@Override
	public void addKeyEventListener(KeyEventListener keyEventListener) {

		Objects.requireNonNull(keyEventListener);

		final SWTKeyEventListener swtKeyEventListener = new SWTKeyEventListener(keyEventListener);
		
		display.addFilter(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				event.doit = swtKeyEventListener.keyPressed(event.character, event.keyCode, event.keyLocation, event.stateMask);
			}
		});
	}

	@Override
	public TypeSuggestion askOpenType(OpenTypeDialogModel model) {
		
		final SWTOpenTypeDialog dialog = new SWTOpenTypeDialog(window, model);
		
		dialog.open();
		
		return dialog.getSuggestion();
	}
	
	@Override
	public NewableSelection askCreateNewable(Collection<NewableCategory> categories) {

		final CreateNewableDialog dialog = new CreateNewableDialog(window, categories);
		
		return dialog.open() == SWT.OK ? dialog.getSelection() : null;
	}
	
	@Override
	public void openNewableDialog(
			InstantiationComponentUI uiComponent,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess) {

		uiComponent.openNewableDialog(dialogUIContext, category, newable, sourceFolder, namespace, ideAccess);
	}

	@Override
	public void askFindReplace(FindReplaceDialogModel lastModel, Consumer<FindReplaceDialog> onCreated) {
		
		final SWTFindReplaceDialog dialog = new SWTFindReplaceDialog(window, lastModel, uiParameters.getTranslator(), onCreated);

		dialog.open();
	}

	@Override
	public ProjectView getProjectView() {
		return projectView;
	}

	@Override
	public EditorsView getEditorsView() {
		return editorsView;
	}

	@Override
	public void minMaxEditors() {
		
		// overviewComposite.setVisible(editorsMaximized);
		overviewGridData.exclude = !editorsMaximized;

		detailsTabFolder.setVisible(editorsMaximized);
		detailsGridData.exclude = !editorsMaximized;
		
		composite.layout(true);

		this.editorsMaximized = !editorsMaximized;
	}

    boolean isClosed() {
        
        return window.isDisposed();
    }
}
