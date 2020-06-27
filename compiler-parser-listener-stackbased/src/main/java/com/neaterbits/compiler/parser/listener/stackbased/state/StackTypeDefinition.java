package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassModifierSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackTypeDefinition<TYPE_DEFINITION, CLASS_MODIFIER_HOLDER> extends ListStackEntry<TYPE_DEFINITION>
    implements ClassModifierSetter<CLASS_MODIFIER_HOLDER> {

    private final List<CLASS_MODIFIER_HOLDER> modifiers;

    
    public StackTypeDefinition(ParseLogger parseLogger) {
        super(parseLogger);

        this.modifiers = new ArrayList<>();
    }

    @Override
    public void addClassModifier(CLASS_MODIFIER_HOLDER modifier) {

        Objects.requireNonNull(modifier);

        modifiers.add(modifier);
    }

    public List<CLASS_MODIFIER_HOLDER> getModifiers() {
        return modifiers;
    }
}
