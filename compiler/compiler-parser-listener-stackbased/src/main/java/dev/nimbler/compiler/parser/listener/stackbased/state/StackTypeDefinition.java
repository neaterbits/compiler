package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ClassModifierSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackTypeDefinition<TYPE_DEFINITION, ANNOTATION, CLASS_MODIFIER_HOLDER> extends ListStackEntry<TYPE_DEFINITION>
    implements AnnotationSetter<ANNOTATION>,
               ClassModifierSetter<CLASS_MODIFIER_HOLDER> {

    private final List<ANNOTATION> annotations;
    private final List<CLASS_MODIFIER_HOLDER> modifiers;

    public StackTypeDefinition(ParseLogger parseLogger) {
        super(parseLogger);

        this.annotations = new ArrayList<>();
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

    @Override
    public void addAnnotation(ANNOTATION annotation) {

        Objects.requireNonNull(annotation);

        annotations.add(annotation);
    }

    public List<ANNOTATION> getAnnotations() {
        return annotations;
    }
}
