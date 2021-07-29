package dev.nimbler.ide.ui.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import dev.nimbler.ide.common.ui.actions.BuiltinActionAccess;
import dev.nimbler.ide.common.ui.actions.TranslatedAction;
import dev.nimbler.ide.common.ui.keys.KeyBindings;
import dev.nimbler.ide.common.ui.menus.BuiltinMenu;
import dev.nimbler.ide.common.ui.menus.MenuBuilder;
import dev.nimbler.ide.common.ui.menus.MenuEntry;
import dev.nimbler.ide.common.ui.menus.SeparatorMenuEntry;
import dev.nimbler.ide.common.ui.menus.SubMenuEntry;
import dev.nimbler.ide.common.ui.translation.Translateable;
import dev.nimbler.ide.ui.actions.BuiltinAction;

final class MenuBuilderImpl implements MenuBuilder {

	private final KeyBindings keyBindings;
	private final List<MenuEntry> entries;
	
	private final Map<BuiltinMenu, SubMenuEntry> builtinMenues;
	
	MenuBuilderImpl(KeyBindings keyBindings) {
		
		Objects.requireNonNull(keyBindings);
		
		this.entries = new ArrayList<>();
		this.keyBindings = keyBindings;
		
		this.builtinMenues = new HashMap<>();
	}

    @Override
    public MenuBuilder addSubMenu(BuiltinMenu builtinMenu, Consumer<MenuBuilder> builder) {
        
        Objects.requireNonNull(builtinMenu);
        
        final SubMenuEntry entry = makeSubMenuEntry(builtinMenu, builder);
        
        builtinMenues.put(builtinMenu, entry);
        
        entries.add(entry);
        
        return this;
    }
    
    private List<MenuEntry> makeSubMenuEntries(Consumer<MenuBuilder> builder) {
        
        final MenuBuilderImpl subBuilder = new MenuBuilderImpl(keyBindings);
        
        builder.accept(subBuilder);

        final List<MenuEntry> subEntries = subBuilder.build();
    
        return subEntries;
    }
    
    private SubMenuEntry makeSubMenuEntry(Translateable translateable, Consumer<MenuBuilder> builder) {
        
        return new SubMenuEntry(translateable, makeSubMenuEntries(builder));
    }
	
	@Override
    public MenuBuilder addSubMenu(Translateable translateable, Consumer<MenuBuilder> builder) {

	    Objects.requireNonNull(translateable);
        
        entries.add(makeSubMenuEntry(translateable, builder));
        
        return this;
    }

    @Override
    public MenuBuilder addSubMenu(String translationNamespace, String translationId, Consumer<MenuBuilder> builder) {

        return addSubMenu(
                Translateable.makeTranslateable(translationNamespace, translationId),
                builder);
    }

    @Override
    public MenuBuilder addToMenu(BuiltinMenu builtinMenu, Consumer<MenuBuilder> builder) {
        
        final SubMenuEntry subMenuEntry = builtinMenues.get(builtinMenu);

        final SubMenuEntry updatedSubMenuEntry = makeSubMenuEntry(builtinMenu, builder);

        final int index = entries.indexOf(subMenuEntry);
        
        entries.set(index, updatedSubMenuEntry);
        
        return this;
    }

    @Override
    public MenuBuilder addAction(TranslatedAction<?, ?> action) {
        
        entries.add(new ActionMenuEntry(action, keyBindings));
        
        return this;
    }

    @Override
	public MenuBuilder addSeparator() {
		
		entries.add(new SeparatorMenuEntry());
	
		return this;
	}

	@Override
	public MenuBuilder addBuiltinAction(BuiltinActionAccess action) {
		
		entries.add(new BuiltinActionMenuEntry((BuiltinAction)action, keyBindings));
		
		return this;
	}
	
    List<MenuEntry> build() {
		return entries;
	}
}
