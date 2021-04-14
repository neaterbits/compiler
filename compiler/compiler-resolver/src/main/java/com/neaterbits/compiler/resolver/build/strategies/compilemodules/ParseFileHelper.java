package com.neaterbits.compiler.resolver.build.strategies.compilemodules;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.build.strategies.compilemodules.PossibleTypeRefs;
import com.neaterbits.compiler.model.common.ElementVisitor;
import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.FileSystemFileSpec;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.language.codemap.compiler.CompilerCodeMap;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.types.TypeVariant;
import com.neaterbits.util.ArrayStack;
import com.neaterbits.util.IntList;
import com.neaterbits.util.Stack;
import com.neaterbits.util.parse.ParserException;

public class ParseFileHelper {

    public static <
        COMPILATION_UNIT,
        PARSED_FILE extends ParsedFile,
        RESOLVE_ERROR>
    
        ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR> parse(
            InputStream stream,
            Charset charset,
            String filename,
            File file,
            Parser<COMPILATION_UNIT> parser,
            IntList types,
            CompilerCodeMap codeMap,
            CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel) throws IOException, ParserException {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(baos);
        
        final List<ParseError> errors = new ArrayList<>();
        
        final COMPILATION_UNIT compilationUnit = parser.parse(
                stream,
                charset,
                errors,
                filename,
                fullContextProvider -> new ParseLogger(printStream, fullContextProvider));
        
        final PARSED_FILE parsedFile = compilerModel.createParsedFile(
                file != null ? new FileSystemFileSpec(file) : null,
                compilationUnit,
                errors.stream().map(Function.identity()).collect(Collectors.toList()),
                new String(baos.toByteArray()));

        final PossibleTypeRefs possibleTypeRefs = findPossibleTypeRefs(
                                                        compilationUnit,
                                                        compilerModel.getCompilationUnitModel(),
                                                        codeMap,
                                                        types);
        
        final int fileNo = file != null
                ? codeMap.addFile(file.getAbsolutePath(), types.toArray())
                : -1;

        return new ParsedWithCachedRefs<>(
                parsedFile,
                possibleTypeRefs,
                fileNo);
    }
    
    private static <COMPILATION_UNIT> PossibleTypeRefs findPossibleTypeRefs(
            COMPILATION_UNIT compilationUnit,
            ParseTreeModel<COMPILATION_UNIT> parseTreeModel,
            CompilerCodeMap codeMap,
            IntList types) {
     
        final PossibleTypeRefs possibleTypeRefs = new PossibleTypeRefs();
        
        final Stack<Scope> stack = new ArrayStack<>();
        
        final ElementVisitor<COMPILATION_UNIT> elementVisitor = new ElementVisitor<COMPILATION_UNIT>() {

            @Override
            public void onElementStart(
                    COMPILATION_UNIT compilationUnit,
                    int parseTreeRef,
                    ParseTreeElement elementType) {
                
                System.out.println("## element start " + elementType);

                switch (elementType) {
                case UNRESOLVED_IDENTIFIER_TYPE_REFERENCE:
                case UNRESOLVED_SCOPED_TYPE_REFERENCE:
                    possibleTypeRefs.addPossibleTypeReference(parseTreeRef);
                    break;
                    
                case NAMESPACE:
                    final List<String> namespace = parseTreeModel.getNamespace(compilationUnit, parseTreeRef);
                    
                    final String [] namespaceStrings = namespace.toArray(new String[namespace.size()]);
                    
                    System.out.println("## name space strings " + Arrays.toString(namespaceStrings));
                    
                    stack.push(new Scope(elementType, namespaceStrings));
                    break;
                    
                case CLASS_METHOD_MEMBER:
                    ++ stack.get().numMethods;
                    break;
                    
                case CLASS_DEFINITION:
                    
                    System.out.println("## class definition with stack " + stack.size());
                    
                    stack.push(new Scope(elementType, parseTreeModel.getClassName(compilationUnit, parseTreeRef)));
                    
                    addType(TypeVariant.CLASS, codeMap, types, stack);
                    break;
                    
                case ENUM_DEFINITION:
                    
                    stack.push(new Scope(elementType, parseTreeModel.getEnumName(compilationUnit, parseTreeRef)));

                    addType(TypeVariant.ENUM, codeMap, types, stack);
                    break;
                    
                case INTERFACE_DEFINITION:
                    
                    stack.push(new Scope(elementType, parseTreeModel.getInterfaceName(compilationUnit, parseTreeRef)));
                    
                    addType(TypeVariant.INTERFACE, codeMap, types, stack);
                    break;
                    
                default:
                    break;
                }
            }

            @Override
            public void onElementEnd(COMPILATION_UNIT compilationUnit, int parseTreeRef, ParseTreeElement elementType) {
                
                System.out.println("## element end " + elementType);

                switch (elementType) {
                
                case NAMESPACE:
                case CLASS_DEFINITION:
                case ENUM_DEFINITION:
                case INTERFACE_DEFINITION:
                    stack.pop();
                    break;
                    
                default:
                    break;
                }
            }
        };

        parseTreeModel.iterateElements(compilationUnit, elementVisitor);

        return possibleTypeRefs;
    }
    
    private static void addType(TypeVariant typeVariant, CompilerCodeMap codeMap, IntList types, Stack<Scope> scopes) {
        
        final Scope classScope = scopes.get(scopes.size() - 1);

        final int typeNo = codeMap.addType(typeVariant, classScope.numMethods, null, null);
        
        types.add(typeNo);
        
        final Scope initialScope = scopes.get(0);
        
        final String [] namespace;

        final int initialNonNameSpace;
        final int nonNamespace;

        System.out.println("## initial element is " + initialScope.parseTreeElement);
        
        if (initialScope.parseTreeElement == ParseTreeElement.NAMESPACE) {

            namespace = initialScope.entries;
            nonNamespace = scopes.size() - 1;
            initialNonNameSpace = 1;
        }
        else {
            namespace = null;
            nonNamespace = scopes.size();
            initialNonNameSpace = 0;
        }
        
        final String [] outerTypes;
        
        if (nonNamespace > 1) {
            final int numOuter = nonNamespace - 1;
                    
            outerTypes = new String[numOuter];

            for (int i = 0; i < numOuter; ++ i) {
                outerTypes[i] = scopes.get(i + initialNonNameSpace).entry;
            }
        }
        else {
            outerTypes = null;
        }
        
        final TypeName typeName = new TypeName(
                namespace,
                outerTypes,
                scopes.get(scopes.size() - 1).entry);

        System.out.println("### add type " + typeVariant + "/" + typeName);

        codeMap.addTypeMapping(typeName, typeNo);
    }

    private static class Scope {
        
        private final ParseTreeElement parseTreeElement;
        
        private final String [] entries;
        
        private final String entry;
        
        private int numMethods;

        public Scope(ParseTreeElement parseTreeElement, String [] entries) {
            
            Objects.requireNonNull(parseTreeElement);
            Objects.requireNonNull(entries);
            
            if (entries.length == 0) {
                throw new IllegalArgumentException();
            }
            
            this.parseTreeElement = parseTreeElement;
            this.entries = entries;
            this.entry = null;
        }
        
        public Scope(ParseTreeElement parseTreeElement, String entry) {
            
            Objects.requireNonNull(parseTreeElement);
            Objects.requireNonNull(entry);
            
            this.parseTreeElement = parseTreeElement;
            this.entry = entry;
            this.entries = null;
        }
    }
}
