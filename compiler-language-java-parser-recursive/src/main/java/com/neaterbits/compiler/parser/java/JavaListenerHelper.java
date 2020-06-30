package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.ParserListener;
import com.neaterbits.compiler.parser.recursive.CachedKeyword;
import com.neaterbits.compiler.parser.recursive.CachedKeywords;
import com.neaterbits.compiler.parser.recursive.TypeArgument;
import com.neaterbits.compiler.parser.recursive.TypeArguments;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.parse.ParserException;

final class JavaListenerHelper<COMPILATION_UNIT> {
    
    @FunctionalInterface
    interface WriteContext {
        int write(int context);
    }
    
    private final ParserListener<COMPILATION_UNIT> listener;
    private final WriteContext writeContext;
    
    JavaListenerHelper(ParserListener<COMPILATION_UNIT> listener, WriteContext writeContext) {
        
        Objects.requireNonNull(listener);
        Objects.requireNonNull(writeContext);
        
        this.listener = listener;
        this.writeContext = writeContext;
    }

    private int writeContext(int context) {

        return writeContext.write(context);
    }

    void onType(
            int typeNameContext,
            long typeName,
            Names scopedTypeName,
            TypeArguments typeArguments,
            ReferenceType referenceType,
            Context endContext) throws IOException, ParserException {
        
        if (scopedTypeName != null) {
            
            final int startContext = callScopedTypeReferenceListenersStartAndPart(scopedTypeName, referenceType);

            // Generic type?
            if (typeArguments != null) {
                onTypeArguments(typeArguments);
            }

            callScopedTypeReferenceListenersEnd(startContext, endContext);
        }
        else if (typeName != StringRef.STRING_NONE) {

            callNonScopedTypeReferenceListenersStart(typeNameContext, typeName, typeArguments, referenceType);

            // Generic type?
            if (typeArguments != null) {
                onTypeArguments(typeArguments);
            }

            callNonScopedTypeReferenceListenersEnd(typeNameContext, endContext, referenceType);
        }
        else {
            throw new IllegalStateException();
        }
    }

    private void onTypeArguments(TypeArguments typeArguments) throws IOException, ParserException {
        
        Objects.requireNonNull(typeArguments);
        
        listener.onGenericTypeParametersStart(typeArguments.getStartContext());
        
        for (int i = 0; i < typeArguments.count(); ++ i) {
            
            final TypeArgument typeArgument = typeArguments.getTypeArgument(i);

            if (typeArgument.isGenericTypeName()) {
                
                listener.onGenericTypeParameter(
                        typeArgument.getGenericTypeNameContext(),
                        typeArgument.getGenericTypeName());
            }
            else {
                typeArgument.getConcreteTypeNames().complete(names -> {
                    
                    if (typeArgument.getConcreteTypeGenerics() != null) {
                        typeArgument.getConcreteTypeGenerics().complete(genericTypes -> {
                            
                            onType(
                                ContextRef.NONE,
                                StringRef.STRING_NONE,
                                names,
                                genericTypes,
                                ReferenceType.REFERENCE,
                                typeArgument.getConcreteEndContext());
                        });
                    
                    }
                    else {
                        onType(
                                ContextRef.NONE,
                                StringRef.STRING_NONE,
                                names,
                                null,
                                ReferenceType.REFERENCE,
                                typeArgument.getConcreteEndContext());
                    }
                });
                
            }
        }
        
        listener.onGenericTypeParametersEnd(typeArguments.getStartContext(), typeArguments.getEndContext());
    }

    int callScopedTypeReferenceListenersStartAndPart(Names scopedTypeName, ReferenceType referenceType) {

        final int startContext = writeContext(scopedTypeName.getContextAt(0));
        
        listener.onScopedTypeReferenceStart(startContext, referenceType);
        
        for (int i = 0; i < scopedTypeName.count(); ++ i) {

            final int context = scopedTypeName.getContextAt(i);
            final long part = scopedTypeName.getStringAt(i);
            
            listener.onScopedTypeReferencePart(context, part);
        }
        
        return startContext;
    }

    void callScopedTypeReferenceListenersEnd(int startContext, Context endContext) {
        
        listener.onScopedTypeReferenceEnd(startContext, endContext);
    }

    void callNonScopedTypeReferenceListenersStart(int typeNameContext, long typeName, TypeArguments typeArguments, ReferenceType referenceType) {

        if (referenceType.isLeaf()) {
            
            if (typeArguments != null) {
                throw new IllegalStateException();
            }
            
            listener.onLeafTypeReference(typeNameContext, typeName, referenceType);
        }
        else {
            listener.onNonScopedTypeReferenceStart(typeNameContext, typeName, referenceType);
        }
    }

    void callNonScopedTypeReferenceListenersEnd(int typeNameContext, Context endContext, ReferenceType referenceType) {

        if (!referenceType.isLeaf()) {
            listener.onNonScopedTypeReferenceEnd(typeNameContext, endContext);
        }
    }

    void callFieldMemberModifiers(CachedKeywords<JavaToken> keywords) throws ParserException {

        for (int i = 0; i < keywords.count(); ++ i) {
            
            final CachedKeyword<JavaToken> keyword = keywords.getKeyword(i);
            
            switch (keyword.getToken()) {
            
            case PRIVATE:
                listener.onVisibilityFieldModifier(keyword.getContext(), FieldVisibility.PRIVATE);
                break;

            case FINAL:
                listener.onMutabilityFieldModifier(keyword.getContext(), ASTMutability.VALUE_OR_REF_IMMUTABLE);
                break;

            default:
                throw new ParserException("Unexpected token " + keyword.getToken());
            }
        }
    }
}
