package com.neaterbits.compiler.parser.java;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.model.objects.ObjectsCompilerModel;

final class ObjectJavaParser extends JavaRecursiveParserHelper<CompilationUnit> {

    public ObjectJavaParser() {
        this(null);
    }

    public ObjectJavaParser(GetBuiltinTypeNo getBuiltinTypeNo) {
        super(ObjectsCompilerModel.createListener(JavaTypes.getBuiltinTypes(), getBuiltinTypeNo));
    }
}
