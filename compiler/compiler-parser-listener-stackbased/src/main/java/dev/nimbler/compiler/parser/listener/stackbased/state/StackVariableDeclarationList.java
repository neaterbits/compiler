package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.VariableModifierSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION, ANNOTATION, MODIFIER_HOLDER>
	extends BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION>
	implements VariableModifierSetter<MODIFIER_HOLDER> {

    private final List<ANNOTATION> annotations;
	private final List<MODIFIER_HOLDER> modifiers;

	public StackVariableDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);

		this.annotations = new ArrayList<>();
		this.modifiers = new ArrayList<>();
	}

	public List<ANNOTATION> getAnnotations() {
        return annotations;
    }

    public List<MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	@Override
	public void addModifier(MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}
}
