package dev.nimbler.compiler.language.java;


import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.parser.ASTParsedFile;
import dev.nimbler.compiler.model.objects.ObjectsCompilerModel;
import dev.nimbler.compiler.resolver.util.CompilerLanguage;

public class JavaCompilerLanguage extends CompilerLanguage<CompilationUnit, ASTParsedFile> {

    public JavaCompilerLanguage(GetBuiltinTypeNo getBuiltinTypeNo) {
        super(
                JavaLanguageSpec.INSTANCE,
                new ObjectsCompilerModel(
                        JavaLanguageSpec.INSTANCE,
                        JavaTypes.getBuiltinTypes(),
                        getBuiltinTypeNo));
    }
}
