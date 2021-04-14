package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.AnnotationElementSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;
import dev.nimbler.language.common.types.ScopedName;

public final class StackAnnotation<ANNOTATION_ELEMENT>
    extends ListStackEntry<ANNOTATION_ELEMENT>
    implements AnnotationElementSetter<ANNOTATION_ELEMENT> {

    private final ScopedName scopedName;
        
	public StackAnnotation(ParseLogger parseLogger, ScopedName scopedName) {
		super(parseLogger);
		
		Objects.requireNonNull(scopedName);
		
		this.scopedName = scopedName;
	}

    public ScopedName getScopedName() {
        return scopedName;
    }

    @Override
    public void addAnnotationElement(ANNOTATION_ELEMENT element) {

        Objects.requireNonNull(element);

        add(element);
    }
}
