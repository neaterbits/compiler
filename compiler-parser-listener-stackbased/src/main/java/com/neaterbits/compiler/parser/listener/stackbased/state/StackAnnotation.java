package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationElementSetter;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
