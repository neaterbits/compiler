package com.neaterbits.compiler.resolver.passes.addfieldspass;

import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.TypeMemberVisitor;
import com.neaterbits.compiler.resolver.passes.BaseTypeVisitor;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.Visibility;

final class AddFieldsVisitor extends BaseTypeVisitor implements TypeMemberVisitor {

    private final CompilerCodeMap codeMap;

    AddFieldsVisitor(CompilerCodeMap codeMap) {
        
        Objects.requireNonNull(codeMap);
        
        this.codeMap = codeMap;
    }

    @Override
    public void onField(
            CharSequence name,
            TypeName fieldTypeName,
            int numArrayDimensions,
            boolean isStatic,
            Visibility visibility, Mutability mutability,
            boolean isVolatile, boolean isTransient, int indexInType) {
        
        if (fieldTypeName != null) { // type is replaced
            
            final int fieldType = codeMap.getTypeNoByTypeName(fieldTypeName);
         
            final AddFieldScope addFieldScope = (AddFieldScope)getScope();
            
            codeMap.addField(
                    addFieldScope.getTypeNo(),
                    name.toString(),
                    fieldType,
                    isStatic, visibility, mutability, isVolatile, isTransient,
                    indexInType);
        }
    }

    @Override
    public void onMethod(
            String name,
            MethodVariant methodVariant,
            TypeName returnType,
            TypeName[] parameterTypes,
            int indexInType) {

        throw new UnsupportedOperationException();
    }

    @Override
    protected void pushType(TypeVariant typeVariant, CharSequence name) {
        
        final TypeName typeName = makeTypeName(name);
        
        final int typeNo = codeMap.getTypeNoByTypeName(typeName);

        pushType(new AddFieldScope(typeVariant, typeName.getName(), typeNo));
    }
}
