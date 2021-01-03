package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.parser.java.JavaParser;

public final class ObjectJavaParser extends JavaParser<CompilationUnit> {

    public ObjectJavaParser() {
        this(null);
    }

    public ObjectJavaParser(GetBuiltinTypeNo getBuiltinTypeNo) {
        super(JavaUtil.createListener(getBuiltinTypeNo));
    }
}
