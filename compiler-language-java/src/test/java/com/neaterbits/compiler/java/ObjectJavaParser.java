package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.parser.java.JavaRecursiveParserHelper;

public final class ObjectJavaParser extends JavaRecursiveParserHelper<CompilationUnit> {

    public ObjectJavaParser() {
        this(null);
    }

    public ObjectJavaParser(GetBuiltinTypeNo getBuiltinTypeNo) {
        super(JavaUtil.createListener(getBuiltinTypeNo));
    }
}
