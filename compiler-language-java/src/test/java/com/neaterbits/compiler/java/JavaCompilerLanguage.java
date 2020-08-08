package com.neaterbits.compiler.java;


import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.resolver.ast.objects.ObjectsCompilerModel;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;

public class JavaCompilerLanguage extends CompilerLanguage<CompilationUnit, ASTParsedFile, CodeMapCompiledAndMappedFiles<CompilationUnit>> {

    public JavaCompilerLanguage() {
        super(JavaLanguageSpec.INSTANCE, new ObjectsCompilerModel());
    }
}
