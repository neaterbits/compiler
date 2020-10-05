package com.neaterbits.compiler.resolver.passes.typefinder;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.TypeVisitor;
import com.neaterbits.util.ArrayStack;

final class TypeFinderVisitor implements TypeVisitor {

    private final CompilerCodeMap codeMap;
    private final Map<ScopedName, TypeName> typeNameByScopedName;
    private final ArrayStack<TypeFinderScope> stack;

    public TypeFinderVisitor(CompilerCodeMap codeMap, Map<ScopedName, TypeName> typeNameByScopedName) {

        Objects.requireNonNull(codeMap);
        Objects.requireNonNull(typeNameByScopedName);

        this.codeMap = codeMap;
        this.typeNameByScopedName = typeNameByScopedName;

        this.stack = new ArrayStack<>();
    }

    @Override
    public void onNamespaceStart() {

        stack.push(new NamespaceScope());
    }

    @Override
    public void onNamespacePart(CharSequence part) {

        final NamespaceScope scope = (NamespaceScope)stack.get();

        scope.addPart(part.toString());
    }

    @Override
    public void onNamespaceEnd() {

        stack.pop();
    }

    private void pushType(TypeVariant typeVariant, CharSequence name) {

        final String nameString = name.toString();

        // Get type scope
        final NamespaceScope namespace = (NamespaceScope)stack.get(0);

        final String [] outerTypes;

        if (stack.size() > 1) {

            final int numOuterTypes = stack.size() - 1;

            outerTypes = new String[numOuterTypes];

            for (int i = 0; i < numOuterTypes; ++ i) {
                final TypeScope outerType = (TypeScope)stack.get(i + 1);

                outerTypes[i] = outerType.getTypeName();
            }
        }
        else {
            outerTypes = null;
        }

        // Add to codemap
        final int typeNo = codeMap.addType(typeVariant);

        // and to stack
        stack.push(new TypeScope(typeVariant, nameString, typeNo));

        // Add typename to code map
        final TypeName typeName = new TypeName(
                namespace.getParts(),
                outerTypes,
                nameString);

        if (typeNameByScopedName.put(typeName.toScopedName(), typeName) != null) {
            throw new IllegalStateException("Already added " + typeName.toScopedName());
        }

        // Map for later resolving of types
        codeMap.addTypeMapping(typeName, typeNo);
    }

    @Override
    public void onClassStart(CharSequence name) {

        pushType(TypeVariant.CLASS, name);
    }

    @Override
    public void onClassEnd() {

        stack.pop();
    }

    @Override
    public void onInterfaceStart(CharSequence name) {

        pushType(TypeVariant.INTERFACE, name);
    }

    @Override
    public void onInterfaceEnd() {

        stack.pop();
    }

    @Override
    public void onEnumStart(CharSequence name) {

        pushType(TypeVariant.ENUM, name);
    }

    @Override
    public void onEnumEnd() {

        stack.pop();
    }
}
