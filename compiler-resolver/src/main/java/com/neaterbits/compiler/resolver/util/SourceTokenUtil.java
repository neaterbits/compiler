package com.neaterbits.compiler.resolver.util;

import java.util.Objects;

import com.neaterbits.compiler.resolver.ScopedNameResolver;
import com.neaterbits.compiler.resolver.TypesMap;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ImportsModel;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.model.SourceToken;
import com.neaterbits.compiler.util.model.SourceTokenType;
import com.neaterbits.compiler.util.model.TypeSources;

public class SourceTokenUtil {
    
    public interface ASTAccess<ELEMENT, COMPILATION_UNIT> {
        
        ParseTreeElement getParseTreeElement(ELEMENT element, COMPILATION_UNIT compilationUnit);
        
        int getParseTreeRef(ELEMENT element, COMPILATION_UNIT compilationUnit);
        
        Context getContext(ELEMENT element, COMPILATION_UNIT compilationUnit);
        
        TypeName getBuiltinTypeReferenceTypeName(ELEMENT element, COMPILATION_UNIT compilationUnit);
        
        ScopedName getResolveLaterReference(ELEMENT element, COMPILATION_UNIT compilationUnit);
        
        ScopedName getReferencedFrom(ELEMENT element, COMPILATION_UNIT compilationUnit);
        
        boolean isPlaceholderElement(ELEMENT element, COMPILATION_UNIT compilationUnit);
    }
    

    public static <ELEMENT, COMPILATION_UNIT> SourceToken makeSourceToken(
            ELEMENT element,
            COMPILATION_UNIT compilationUnit,
            ResolvedTypes resolvedTypes,
            ImportsModel<COMPILATION_UNIT> importsModel,
            ASTAccess<ELEMENT, COMPILATION_UNIT> astAccess) {
        
        final SourceToken sourceToken;
        
        if (astAccess.isPlaceholderElement(element, compilationUnit)) {
            sourceToken = new SourceToken(element.getClass().getSimpleName());
        }
        else {
            sourceToken = makeSourceTokenForNonPlaceholder(element, compilationUnit, resolvedTypes, importsModel, astAccess);
        }
        
        return sourceToken;
    }

    private static <ELEMENT, COMPILATION_UNIT> SourceToken makeSourceTokenForNonPlaceholder(
            ELEMENT element,
            COMPILATION_UNIT compilationUnit,
            ResolvedTypes resolvedTypes,
            ImportsModel<COMPILATION_UNIT> importsModel,
            ASTAccess<ELEMENT, COMPILATION_UNIT> astAccess) {
        
        Objects.requireNonNull(element);

        final SourceTokenType sourceTokenType;
        TypeName typeName = null;
        
        switch (astAccess.getParseTreeElement(element, compilationUnit)) {
        case KEYWORD:
            sourceTokenType = SourceTokenType.KEYWORD;
            break;
            
        case INTERFACE_MODIFIER_HOLDER:
        case CLASS_MODIFIER_HOLDER:
        case CONSTRUCTOR_MODIFIER_HOLDER:
        case CLASS_METHOD_MODIFIER_HOLDER:
        case FIELD_MODIFIER_HOLDER:
        case VARIABLE_MODIFIER_HOLDER:
            sourceTokenType = SourceTokenType.KEYWORD;
            break;

        case CHARACTER_LITERAL:
            sourceTokenType = SourceTokenType.CHARACTER_LITERAL;
            break;

        case STRING_LITERAL:
            sourceTokenType = SourceTokenType.STRING_LITERAL;
            break;

        case INTEGER_LITERAL:
            sourceTokenType = SourceTokenType.INTEGER_LITERAL;
            break;

        case BOOLEAN_LITERAL:
            sourceTokenType = SourceTokenType.BOOLEAN_LITERAL;
            break;

        case NULL_LITERAL:
            sourceTokenType = SourceTokenType.NULL_LITERAL;
            break;

        case THIS_PRIMARY:
            sourceTokenType = SourceTokenType.THIS_REFERENCE;
            break;

        case VAR_NAME_DECLARATION:
            sourceTokenType = SourceTokenType.LOCAL_VARIABLE_DECLARATION_NAME;
            break;
            
        case NAME_REFERENCE:
            sourceTokenType = SourceTokenType.VARIABLE_REFERENCE;
            break;

        case NAMESPACE_DECLARATION:
            sourceTokenType = SourceTokenType.NAMESPACE_DECLARATION_NAME;
            break;
            
        case IMPORT_NAME:
            sourceTokenType = SourceTokenType.IMPORT_NAME;
            break;

        case CLASS_DECLARATION_NAME:
            sourceTokenType = SourceTokenType.CLASS_DECLARATION_NAME;
            break;

        case FIELD_NAME_DECLARATION:
            sourceTokenType = SourceTokenType.INSTANCE_VARIABLE_DECLARATION_NAME;
            break;
            
        case INTERFACE_DECLARATION_NAME:
            sourceTokenType = SourceTokenType.INTERFACE_DECLARATION_NAME;
            break;

        case INTERFACE_METHOD_NAME:
            sourceTokenType = SourceTokenType.METHOD_DECLARATION_NAME;
            break;

        case SCALAR_TYPE_REFERENCE:
            sourceTokenType = SourceTokenType.BUILTIN_TYPE_NAME;
            
            typeName = astAccess.getBuiltinTypeReferenceTypeName(element, compilationUnit);
            break;

        case ENUM_CONSTANT:
            sourceTokenType = SourceTokenType.ENUM_CONSTANT;
            break;

        case RESOLVE_LATER_TYPE_REFERENCE:

            // Resolve from already resolved types
            final TypesMap<TypeName> compiledTypesMap = new TypesMap<TypeName>() {

                @Override
                public TypeName lookupByScopedName(ScopedName scopedName) {
                    return resolvedTypes.lookup(scopedName, TypeSources.ALL);
                }
            };

            // TODO support multiple classes in one CompilationUnit
            
            final ScopedName referencedFrom = astAccess.getReferencedFrom(element, compilationUnit);
            
            if (referencedFrom != null) {
            
                final TypeName resolved = ScopedNameResolver.resolveScopedName(
                        astAccess.getResolveLaterReference(element, compilationUnit),
                        null,
                        compilationUnit,
                        importsModel,
                        referencedFrom,
                        compiledTypesMap);
                
                if (resolved != null) {
                    sourceTokenType = SourceTokenType.CLASS_REFERENCE_NAME;
                    typeName = resolved;
                }
                else {
                    sourceTokenType = SourceTokenType.UNKNOWN;
                }
            }
            else {
                sourceTokenType = SourceTokenType.UNKNOWN;
            }
            break;
            
            default:
                sourceTokenType = SourceTokenType.UNKNOWN;
                break;
        }
        
        final Context context = astAccess.getContext(element, compilationUnit);

        return new SourceToken(
                astAccess.getParseTreeRef(element, compilationUnit),
                sourceTokenType,
                context,
                typeName,
                element.getClass().getSimpleName());
    }
}
