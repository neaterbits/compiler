package dev.nimbler.compiler.language.java.parser;

import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.language.java.JavaTypes;
import dev.nimbler.compiler.model.objects.ObjectsCompilerModel;
import dev.nimbler.compiler.parser.java.recursive.JavaRecursiveParserHelper;

final class ObjectJavaParser extends JavaRecursiveParserHelper<CompilationUnit> {

    public ObjectJavaParser() {
        this(null);
    }

    public ObjectJavaParser(GetBuiltinTypeNo getBuiltinTypeNo) {
        super(ObjectsCompilerModel.createListener(JavaTypes.getBuiltinTypes(), getBuiltinTypeNo));
    }
}
