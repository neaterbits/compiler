package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

import com.neaterbits.compiler.parser.java.JavaTypeArgumentsLexerParser.ParseFunction;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;
import com.neaterbits.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import com.neaterbits.compiler.parser.recursive.cached.expressions.ContextWriter;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeyword;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeywords;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeywordsList;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgument;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArguments;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsList;
import com.neaterbits.compiler.types.ReferenceType;
import com.neaterbits.compiler.types.statement.ASTMutability;
import com.neaterbits.compiler.types.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.types.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.types.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.parse.IToken;
import com.neaterbits.util.parse.ParserException;
import com.neaterbits.util.parse.context.Context;

final class JavaListenerHelper<COMPILATION_UNIT> {
    
    private final ParseTreeListener<COMPILATION_UNIT> listener;
    private final ContextWriter contextWriter;
    
    JavaListenerHelper(ParseTreeListener<COMPILATION_UNIT> listener, ContextWriter contextWriter) {
        
        Objects.requireNonNull(listener);
        Objects.requireNonNull(contextWriter);
        
        this.listener = listener;
        this.contextWriter = contextWriter;
    }

    private int writeContext(int context) {

        return contextWriter.writeContext(context);
    }
    
    @FunctionalInterface
    interface ModifiersProcessor<TOKEN extends IToken> {
        
        void process(CachedKeywords<TOKEN> keywords) throws ParserException;
    }

    void onMemberVariableDeclaration(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            TypeScratchInfo typeName,
            Context typeEndContext,
            TypeArgumentsList typeArguments,
            long identifier,
            int identifierContext,
            int numDims,
            Context variableDeclaratorEndContext) throws IOException, ParserException {
        
        onDeclaration(
                modifiers,
                annotations,
                this::callFieldMemberModifiers,
                typeName,
                typeEndContext,
                typeArguments,
                identifier,
                identifierContext,
                numDims,
                variableDeclaratorEndContext);
    }

    void onLocalVariableDeclaration(
            TypeScratchInfo typeName,
            Context typeEndContext,
            TypeArgumentsList typeArguments,
            long identifier,
            int identifierContext,
            int numDims,
            Context variableDeclaratorEndContext) throws IOException, ParserException {
        
        onDeclaration(null, null, null, typeName, typeEndContext, typeArguments, identifier, identifierContext, numDims, variableDeclaratorEndContext);
    }

    void onTypeAndOptionalArgumentsList(TypeScratchInfo typeName, TypeArgumentsList typeArguments, Context typeEndContext) throws IOException, ParserException {
        
        if (typeArguments != null) {
            typeArguments.complete(genericTypes -> onType(typeName, genericTypes, typeEndContext));
        }
        else {
            onType(typeName, null, typeEndContext);
        }
    }
    
    void onModifiersAndType(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            ModifiersProcessor<JavaToken> outputModifiers,
            TypeScratchInfo typeName,
            Context typeEndContext,
            TypeArgumentsList typeArguments) throws IOException, ParserException {
        
        if (modifiers != null) {
            modifiers.complete(keywords -> {
                outputModifiers.process(keywords);
            });
        }

        onTypeAndOptionalArgumentsList(typeName, typeArguments, typeEndContext);
    }
    
    private void onDeclaration(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            ModifiersProcessor<JavaToken> outputModifiers,
            TypeScratchInfo typeName,
            Context typeEndContext,
            TypeArgumentsList typeArguments,
            long identifier,
            int identifierContext,
            int numDims,
            Context variableDeclaratorEndContext) throws IOException, ParserException {
 
        onModifiersAndType(modifiers, annotations, outputModifiers, typeName, typeEndContext, typeArguments);
         
        final int variableDeclaratorStartContext = writeContext(identifierContext);
        
        onVariableDeclarator(
                variableDeclaratorStartContext,
                identifier,
                identifierContext,
                numDims,
                variableDeclaratorEndContext);
    }

    void onType(TypeScratchInfo typeName, TypeArguments typeArguments, Context endContext) throws IOException, ParserException {
        
        Objects.requireNonNull(endContext);
        
        if (typeName.isScoped()) {
            onType(
                    ContextRef.NONE,
                    StringRef.STRING_NONE,
                    typeName.getScoped(),
                    typeName.getAfterTypeContext(),
                    typeArguments,
                    typeName.getReferenceType(),
                    endContext);
        }
        else {
            onType(
                    typeName.getNonScopedContext(),
                    typeName.getNonScopedName(),
                    null,
                    typeName.getAfterTypeContext(),
                    typeArguments,
                    typeName.getReferenceType(),
                    endContext);
        }
    }

    private void onType(
            int typeNameContext,
            long typeName,
            Names scopedTypeName,
            Context namesEndContext,
            TypeArguments typeArguments,
            ReferenceType referenceType,
            Context endContext) throws IOException, ParserException {
        
        if (scopedTypeName != null) {
            
            final int startContext = callScopedTypeReferenceListenersStartAndPart(scopedTypeName, referenceType, namesEndContext);

            // Generic type?
            if (typeArguments != null) {
                applyTypeArguments(typeArguments);
            }

            callScopedTypeReferenceListenersEnd(startContext, endContext);
        }
        else if (typeName != StringRef.STRING_NONE) {

            callNonScopedTypeReferenceListenersStart(typeNameContext, typeName, typeArguments, referenceType);

            // Generic type?
            if (typeArguments != null) {
                applyTypeArguments(typeArguments);
            }

            callNonScopedTypeReferenceListenersEnd(typeNameContext, endContext, referenceType);
        }
        else {
            throw new IllegalStateException();
        }
    }

    private void applyTypeArguments(TypeArguments typeArguments) throws IOException, ParserException {
        
        Objects.requireNonNull(typeArguments);
        
        listener.onGenericTypeArgumentsStart(typeArguments.getStartContext());
        
        for (int i = 0; i < typeArguments.count(); ++ i) {
            
            final TypeArgument typeArgument = typeArguments.getTypeArgument(i);
            
            switch (typeArgument.getType()) {
                
            case REFERENCE: {
                
                final int startContext = typeArgument.getReferenceStartContext();
                
                listener.onGenericReferenceTypeArgumentStart(startContext);
                
                typeArgument.getReferenceTypeNames().complete(names -> {
                    
                    if (typeArgument.getReferenceTypeGenerics() != null) {
                        typeArgument.getReferenceTypeGenerics().complete(genericTypes -> {
                            
                            onType(
                                ContextRef.NONE,
                                StringRef.STRING_NONE,
                                names,
                                null,
                                genericTypes,
                                ReferenceType.REFERENCE,
                                typeArgument.getReferenceEndContext());
                        });
                    
                    }
                    else {
                        onType(
                                ContextRef.NONE,
                                StringRef.STRING_NONE,
                                names,
                                null,
                                null,
                                ReferenceType.REFERENCE,
                                typeArgument.getReferenceEndContext());
                    }
                });

                listener.onGenericReferenceTypeArgumentEnd(startContext, typeArgument.getReferenceEndContext());
                break;
            }
                
            case WILDCARD: {

                final int startContext = typeArgument.getWildcardStartContext();
                
                listener.onGenericWildcardTypeArgumentStart(startContext);
                
                listener.onGenericWildcardTypeArgumentEnd(startContext, typeArgument.getWildcardEndContext());
                break;
            }

            default:
                throw new UnsupportedOperationException();
            }
        }
        
        listener.onGenericTypeArgumentsEnd(typeArguments.getStartContext(), typeArguments.getEndContext());
    }
    
    void onVariableDeclarator(
            int variableDeclaratorStartContext,
            long varName,
            int varNameContext,
            int numDims,
            Context variableDeclaratorEndContext) {

        // Does not call Supplier based method version below in order to save an allocation for closure
        listener.onVariableDeclaratorStart(variableDeclaratorStartContext);
        listener.onVariableName(varNameContext, varName, numDims);
        listener.onVariableDeclaratorEnd(variableDeclaratorStartContext, variableDeclaratorEndContext);
    }

    void onVariableDeclarator(
            int variableDeclaratorStartContext,
            long varName,
            int varNameContext,
            ParseFunction parseInitializer,
            Supplier<Context> variableDeclaratorEndContext) throws IOException, ParserException {
        
        listener.onVariableDeclaratorStart(variableDeclaratorStartContext);
        listener.onVariableName(varNameContext, varName, 0);
        
        parseInitializer.parse();
        
        listener.onVariableDeclaratorEnd(variableDeclaratorStartContext, variableDeclaratorEndContext.get());
    }

    int callScopedTypeReferenceListenersStartAndPart(Names scopedTypeName, ReferenceType referenceType, Context endContext) {

        final int startContext = writeContext(scopedTypeName.getContextAt(0));
        
        listener.onScopedTypeReferenceStart(startContext, referenceType);
        
        final int namesStartContext = writeContext(startContext);
        
        listener.onScopedTypeReferenceNameStart(startContext);
        
        for (int i = 0; i < scopedTypeName.count(); ++ i) {

            final int context = scopedTypeName.getContextAt(i);
            final long part = scopedTypeName.getStringAt(i);
            
            listener.onScopedTypeReferenceNamePart(context, part);
        }

        listener.onScopedTypeReferenceNameEnd(namesStartContext, endContext);

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
                
            case STATIC:
                listener.onStaticFieldModifier(keyword.getContext());
                break;
                
            case VOLATILE:
                listener.onVolatileFieldModifier(keyword.getContext());
                break;
                
            case TRANSIENT:
                listener.onTransientFieldModifier(keyword.getContext());
                break;

            default:
                throw new ParserException("Unexpected token " + keyword.getToken());
            }
        }
    }

    void callClassMethodMemberModifiers(CachedKeywords<JavaToken> keywords) throws ParserException {

        for (int i = 0; i < keywords.count(); ++ i) {
            
            final CachedKeyword<JavaToken> keyword = keywords.getKeyword(i);
            
            switch (keyword.getToken()) {
            
            case PRIVATE:
                listener.onVisibilityClassMethodModifier(keyword.getContext(), ClassMethodVisibility.PRIVATE);
                break;
                
            case PUBLIC:
                listener.onVisibilityClassMethodModifier(keyword.getContext(), ClassMethodVisibility.PUBLIC);
                break;

            case STATIC:
                listener.onStaticClassMethodModifier(keyword.getContext());
                break;

            case FINAL:
                listener.onOverrideClassMethodModifier(keyword.getContext(), ClassMethodOverride.FINAL);
                break;

            default:
                throw new ParserException("Unexpected token " + keyword.getToken());
            }
        }
    }
}

