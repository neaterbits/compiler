package com.neaterbits.compiler.java;

import java.util.Arrays;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.parser.listener.common.CreateParserListener;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.parser.listener.common.ListContextAccess;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.StringSourceFullContextProvider;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

public class JavaUtil {

	public static TypeName parseToTypeName(String type) {
		final String [] parts = Strings.split(type, '.');
		
		final TypeName typeName = new TypeName(
				parts.length > 1 ? Arrays.copyOf(parts, parts.length - 1) : null,
				null,
				parts[parts.length - 1]);
		
		return typeName;
	}

    public static CreateParserListener<CompilationUnit>
    createListener(GetBuiltinTypeNo getBuiltinTypeNo) {

        final ASTParseTreeFactory parseTreeFactory
            = new ASTParseTreeFactory(JavaTypes.getBuiltinTypes(), getBuiltinTypeNo);
        
        return (file, stringBuffers) -> makeListener(file, stringBuffers, parseTreeFactory);
    }
    
    private static IterativeParseTreeListener<CompilationUnit> makeListener(
            String file,
            StringSource stringSource,
            ASTParseTreeFactory parseTreeFactory) {

        final FullContextProvider fullContextProvider = new StringSourceFullContextProvider(file, stringSource);
        
        final ParseLogger parseLogger = new ParseLogger(System.out, fullContextProvider);

        @SuppressWarnings("unchecked")
        final IterativeParseTreeListener<CompilationUnit> listener = new JavaIterativeListener(
                stringSource,
                new ListContextAccess(),
                fullContextProvider,
                parseLogger,
                parseTreeFactory);

        return listener;
    }
}
