package com.neaterbits.compiler.resolver.passes.typefinder;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.TypeVisitor;
import com.neaterbits.compiler.resolver.passes.BaseTypeVisitor;

public class TypeFinderVisitor extends BaseTypeVisitor implements TypeVisitor {

    private final CompilerCodeMap codeMap;
    private final Map<ScopedName, TypeName> typeNameByScopedName;

    public TypeFinderVisitor(CompilerCodeMap codeMap, Map<ScopedName, TypeName> typeNameByScopedName) {

        Objects.requireNonNull(codeMap);
        Objects.requireNonNull(typeNameByScopedName);

        this.codeMap = codeMap;
        this.typeNameByScopedName = typeNameByScopedName;
    }

    @Override
    protected void pushType(TypeVariant typeVariant, CharSequence name) {

        final TypeName typeName = makeTypeName(name);

        // Add to codemap
        final int typeNo = codeMap.addType(typeVariant);

        // and to stack
        pushType(new FoundTypeScope(typeVariant, typeName.getName(), typeNo));


        if (typeNameByScopedName.put(typeName.toScopedName(), typeName) != null) {
            throw new IllegalStateException("Already added " + typeName.toScopedName());
        }

        // Map for later resolving of types
        codeMap.addTypeMapping(typeName, typeNo);
    }
}
