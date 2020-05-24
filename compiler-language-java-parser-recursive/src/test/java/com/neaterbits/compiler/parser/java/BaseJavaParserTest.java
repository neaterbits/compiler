package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationCode;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Import;
import com.neaterbits.compiler.ast.objects.Namespace;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseJavaParserTest {

    static {
        BaseASTElement.REQUIRE_CONTEXT = false;
    }

    abstract CompilationUnit parse(String source) throws IOException, ParserException;

    @Test
    public void testParseNamespace() throws IOException, ParserException {
        
        final String source = "package com.test;\n";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final CompilationCode next = compilationUnit.getCode().iterator().next();
        
        final Namespace namespace = (Namespace)next;

        assertThat(namespace.getParts().length).isEqualTo(2);
        assertThat(namespace.getParts()[0]).isEqualTo("com");
        assertThat(namespace.getParts()[1]).isEqualTo("test");
    }

    @Test
    public void testParseClassImport() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                + ""
                + "import com.test.importtest.TestClass;\n";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getImports().size()).isEqualTo(1);
        
        final Import importEntry = compilationUnit.getImports().iterator().next();
        
        assertThat(importEntry.getKeyword().getText()).isEqualTo("import");
        assertThat(importEntry.getPackage().getMethod()).isNull();
        assertThat(importEntry.getPackage().isMethodImport()).isFalse();
        assertThat(importEntry.getPackage().getTypeName().getName()).isEqualTo("TestClass");
        assertThat(importEntry.getPackage().getNamespaceOrTypeName()).isNull();
        assertThat(importEntry.getPackage().isOnDemandImport()).isFalse();
        assertThat(importEntry.getPackage().getNamespace().getParts().length).isEqualTo(3);
        assertThat(importEntry.getPackage().getNamespace().getParts()[0]).isEqualTo("com");
        assertThat(importEntry.getPackage().getNamespace().getParts()[1]).isEqualTo("test");
        assertThat(importEntry.getPackage().getNamespace().getParts()[2]).isEqualTo("importtest");
    }

    @Test
    public void testParsePackageOnDemandImport() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                + ""
                + "import com.test.importtest.*;\n";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getImports().size()).isEqualTo(1);
        
        final Import importEntry = compilationUnit.getImports().iterator().next();
        
        assertThat(importEntry.getKeyword().getText()).isEqualTo("import");
        assertThat(importEntry.getPackage().getMethod()).isNull();
        assertThat(importEntry.getPackage().isMethodImport()).isFalse();
        assertThat(importEntry.getPackage().getTypeName()).isNull();
        assertThat(importEntry.getPackage().getNamespaceOrTypeName()).isEqualTo(new String [] { "com", "test", "importtest" });
        assertThat(importEntry.getPackage().isOnDemandImport()).isTrue();
        assertThat(importEntry.getPackage().getNamespace()).isNull();
    }

    @Test
    public void testParseClassOnDemandImport() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                + ""
                + "import com.test.importtest.TestClass.*;\n";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getImports().size()).isEqualTo(1);
        
        final Import importEntry = compilationUnit.getImports().iterator().next();
        
        assertThat(importEntry.getKeyword().getText()).isEqualTo("import");
        assertThat(importEntry.getPackage().getMethod()).isNull();
        assertThat(importEntry.getPackage().isMethodImport()).isFalse();
        assertThat(importEntry.getPackage().getTypeName()).isNull();
        assertThat(importEntry.getPackage().getNamespaceOrTypeName()).isEqualTo(new String [] { "com", "test", "importtest", "TestClass" });
        assertThat(importEntry.getPackage().isOnDemandImport()).isTrue();
        assertThat(importEntry.getPackage().getNamespace()).isNull();
    }

    @Test
    public void testParseStaticOnDemandStaticImport() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                + ""
                + "import static com.test.importtest.TestClass.*;\n";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getImports().size()).isEqualTo(1);
        
        final Import importEntry = compilationUnit.getImports().iterator().next();
        
        assertThat(importEntry.getKeyword().getText()).isEqualTo("import");
        assertThat(importEntry.getPackage().getMethod()).isNull();
        assertThat(importEntry.getPackage().isMethodImport()).isTrue();
        assertThat(importEntry.getPackage().getTypeName().getName()).isEqualTo("TestClass");
        assertThat(importEntry.getPackage().getNamespaceOrTypeName()).isNull();
        assertThat(importEntry.getPackage().getNamespace().getParts().length).isEqualTo(3);
        assertThat(importEntry.getPackage().getNamespace().getParts()[0]).isEqualTo("com");
        assertThat(importEntry.getPackage().getNamespace().getParts()[1]).isEqualTo("test");
        assertThat(importEntry.getPackage().getNamespace().getParts()[2]).isEqualTo("importtest");
    }

    @Test
    public void testParseStaticMethodStaticImport() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                + ""
                + "import static com.test.importtest.TestClass.someMethod;\n";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getImports().size()).isEqualTo(1);
        
        final Import importEntry = compilationUnit.getImports().iterator().next();
        
        assertThat(importEntry.getKeyword().getText()).isEqualTo("import");
        assertThat(importEntry.getPackage().getMethod().getName()).isEqualTo("someMethod");
        assertThat(importEntry.getPackage().isMethodImport()).isTrue();
        assertThat(importEntry.getPackage().getTypeName().getName()).isEqualTo("TestClass");
        assertThat(importEntry.getPackage().getNamespaceOrTypeName()).isNull();
        assertThat(importEntry.getPackage().isOnDemandImport()).isFalse();
        assertThat(importEntry.getPackage().getNamespace().getParts().length).isEqualTo(3);
        assertThat(importEntry.getPackage().getNamespace().getParts()[0]).isEqualTo("com");
        assertThat(importEntry.getPackage().getNamespace().getParts()[1]).isEqualTo("test");
        assertThat(importEntry.getPackage().getNamespace().getParts()[2]).isEqualTo("importtest");
    }

    @Test
    public void testParseClassWithoutModifiers() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseFinalClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "final class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().hasModifier(Subclassing.FINAL)).isTrue();
        assertThat(classDefinition.getModifiers().count()).isEqualTo(1);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseAbstractClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "abstract class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().hasModifier(Subclassing.ABSTRACT)).isTrue();
        assertThat(classDefinition.getModifiers().count()).isEqualTo(1);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParsePublicClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "public class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().hasModifier(ClassVisibility.PUBLIC)).isTrue();
        assertThat(classDefinition.getModifiers().count()).isEqualTo(1);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParsePublicFinalClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "public final class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().hasModifier(ClassVisibility.PUBLIC)).isTrue();
        assertThat(classDefinition.getModifiers().hasModifier(Subclassing.FINAL)).isTrue();
        assertThat(classDefinition.getModifiers().count()).isEqualTo(2);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseFinalPublicClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "final public class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().hasModifier(ClassVisibility.PUBLIC)).isTrue();
        assertThat(classDefinition.getModifiers().hasModifier(Subclassing.FINAL)).isTrue();
        assertThat(classDefinition.getModifiers().count()).isEqualTo(2);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParsePublicAbstractClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "public abstract class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().hasModifier(ClassVisibility.PUBLIC)).isTrue();
        assertThat(classDefinition.getModifiers().hasModifier(Subclassing.ABSTRACT)).isTrue();
        assertThat(classDefinition.getModifiers().count()).isEqualTo(2);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseAbstractPublicClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "abstract public class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().hasModifier(ClassVisibility.PUBLIC)).isTrue();
        assertThat(classDefinition.getModifiers().hasModifier(Subclassing.ABSTRACT)).isTrue();
        assertThat(classDefinition.getModifiers().count()).isEqualTo(2);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseClassExtends() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass extends OtherClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses().size()).isEqualTo(1);
        
        final ResolveLaterTypeReference typeRef = (ResolveLaterTypeReference)classDefinition.getExtendsClasses().get(0);
        assertThat(typeRef.getScopedName().getScope()).isNull();
        assertThat(typeRef.getScopedName().getName()).isEqualTo("OtherClass");

        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseClassImplementsOne() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass implements SomeInterface { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        
        assertThat(classDefinition.getImplementsInterfaces().size()).isEqualTo(1);
        final ResolveLaterTypeReference typeRef = (ResolveLaterTypeReference)classDefinition.getImplementsInterfaces().get(0);
        assertThat(typeRef.getScopedName().getScope()).isNull();
        assertThat(typeRef.getScopedName().getName()).isEqualTo("SomeInterface");

        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseClassImplementsMultiple() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass implements SomeInterface, AnotherInterface, com.test.YetAnInterface { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        
        assertThat(classDefinition.getImplementsInterfaces().size()).isEqualTo(3);
        
        ResolveLaterTypeReference typeRef = (ResolveLaterTypeReference)classDefinition.getImplementsInterfaces().get(0);
        assertThat(typeRef.getScopedName().getScope()).isNull();
        assertThat(typeRef.getScopedName().getName()).isEqualTo("SomeInterface");
        
        typeRef = (ResolveLaterTypeReference)classDefinition.getImplementsInterfaces().get(1);
        assertThat(typeRef.getScopedName().getScope()).isNull();
        assertThat(typeRef.getScopedName().getName()).isEqualTo("AnotherInterface");

        typeRef = (ResolveLaterTypeReference)classDefinition.getImplementsInterfaces().get(2);
        assertThat(typeRef.getScopedName().getScope()).isEqualTo(Arrays.asList("com", "test"));
        assertThat(typeRef.getScopedName().getName()).isEqualTo("YetAnInterface");
        
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseClassExtendsAndImplements() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass extends OtherClass implements SomeInterface { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses().size()).isEqualTo(1);
        
        ResolveLaterTypeReference typeRef = (ResolveLaterTypeReference)classDefinition.getExtendsClasses().get(0);
        assertThat(typeRef.getScopedName().getScope()).isNull();
        assertThat(typeRef.getScopedName().getName()).isEqualTo("OtherClass");

        assertThat(classDefinition.getImplementsInterfaces().size()).isEqualTo(1);
        typeRef = (ResolveLaterTypeReference)classDefinition.getImplementsInterfaces().get(0);
        assertThat(typeRef.getScopedName().getScope()).isNull();
        assertThat(typeRef.getScopedName().getName()).isEqualTo("SomeInterface");
        
        assertThat(classDefinition.getMembers()).isEmpty();
    }
}
