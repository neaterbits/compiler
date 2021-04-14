package com.neaterbits.compiler.model.common;

import java.util.Objects;

import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.types.TypeVariant;
import com.neaterbits.util.ArrayStack;

public abstract class BaseTypeVisitor implements TypeVisitor {

    private final ArrayStack<TypeVisitorScope> stack;

    public BaseTypeVisitor() {

        this.stack = new ArrayStack<>();
    }

    @Override
    public final void onNamespaceStart() {

        stack.push(new NamespaceScope());
    }

    @Override
    public final void onNamespacePart(CharSequence part) {

        final NamespaceScope scope = (NamespaceScope)stack.get();

        scope.addPart(part.toString());
    }

    @Override
    public final void onNamespaceEnd() {

        stack.pop();
    }
    
    protected final TypeName makeTypeName(CharSequence name) {
        
        final String nameString = name.toString();

        // Get type scope
        final NamespaceScope namespace = (NamespaceScope)stack.get(0);

        final String [] outerTypes;

        if (stack.size() > 1) {

            final int numOuterTypes = stack.size() - 1;

            outerTypes = new String[numOuterTypes];

            for (int i = 0; i < numOuterTypes; ++ i) {
                final TypeScope outerType = (TypeScope)stack.get(i + 1);

                outerTypes[i] = outerType.getName();
            }
        }
        else {
            outerTypes = null;
        }
        
        // Add typename to code map
        final TypeName typeName = new TypeName(
                namespace.getParts(),
                outerTypes,
                nameString);
        
        return typeName;
    }
    
    protected final TypeVisitorScope getScope() {

        return stack.get();
    }
    
    protected final void pushType(TypeVisitorScope typeVisitorScope) {
        
        Objects.requireNonNull(typeVisitorScope);
        
        stack.push(typeVisitorScope);
    }

    protected void pushType(TypeVariant typeVariant, CharSequence name) {

        pushType(new TypeScope(typeVariant, name.toString()));
    }

    @Override
    public final void onClassStart(CharSequence name) {

        pushType(TypeVariant.CLASS, name);
    }

    @Override
    public final void onClassEnd() {
        
        stack.pop();
    }

    @Override
    public final void onInterfaceStart(CharSequence name) {

        pushType(TypeVariant.INTERFACE, name);
    }

    @Override
    public final void onInterfaceEnd() {

        stack.pop();
    }

    @Override
    public final void onEnumStart(CharSequence name) {

        pushType(TypeVariant.ENUM, name);
    }

    @Override
    public final void onEnumEnd() {

        stack.pop();
    }
}
