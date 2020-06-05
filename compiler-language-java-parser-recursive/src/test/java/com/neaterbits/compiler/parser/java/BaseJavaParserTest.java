package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationCode;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Import;
import com.neaterbits.compiler.ast.objects.Namespace;
import com.neaterbits.compiler.ast.objects.block.ClassMethod;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.objects.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.ScalarTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
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
        
        checkIdentifierType(classDefinition.getExtendsClasses().get(0), "OtherClass");

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
        checkIdentifierType(classDefinition.getImplementsInterfaces().get(0), "SomeInterface");

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
        
        checkIdentifierType(classDefinition.getImplementsInterfaces().get(0), "SomeInterface");
        
        checkIdentifierType(classDefinition.getImplementsInterfaces().get(1), "AnotherInterface");

        checkScopedType(classDefinition.getImplementsInterfaces().get(2), "com", "test", "YetAnInterface");
        
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
        checkIdentifierType(classDefinition.getExtendsClasses().get(0), "OtherClass");

        assertThat(classDefinition.getImplementsInterfaces().size()).isEqualTo(1);
        checkIdentifierType(classDefinition.getImplementsInterfaces().get(0), "SomeInterface");
        
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseScalarClassMemberVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int memberVariable; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassDataFieldMember member = (ClassDataFieldMember)classDefinition.getMembers().get(0);
    
        assertThat(member.getInitializer(0).getNameString()).isEqualTo("memberVariable");
        
        checkScalarType(member.getType(), "int");
    }

    @Test
    public void testParseIdentifierClassMemberVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { SomeType memberVariable; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassDataFieldMember member = (ClassDataFieldMember)classDefinition.getMembers().get(0);
    
        assertThat(member.getInitializer(0).getNameString()).isEqualTo("memberVariable");
        
        checkIdentifierType(member.getType(), "SomeType");
    }

    @Test
    public void testParseCommaSeparatedClassMemberVariables() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int a, b, c; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassDataFieldMember member = (ClassDataFieldMember)classDefinition.getMembers().get(0);
        final ASTList<InitializerVariableDeclarationElement> initializers = member.getInitializers();

        checkScalarType(member.getType(), "int");

        assertThat(initializers.get(0).getNameString()).isEqualTo("a");
        
        assertThat(initializers.get(1).getNameString()).isEqualTo("b");
        
        assertThat(initializers.get(2).getNameString()).isEqualTo("c");
    }

    @Test
    public void testMethodWithoutParameters() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "int");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().isEmpty()).isTrue();
    }

    @Test
    public void testMethodWithReferenceReturnType() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { SomeType someMethod() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        checkIdentifierType(method.getReturnType(), "SomeType");
        
        assertThat(method.getParameters().isEmpty()).isTrue();
    }

    @Test
    public void testMethodWithScopedReferenceReturnType() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { com.test.SomeType someMethod() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();

        checkScopedType(method.getReturnType(), "com", "test", "SomeType");
        
        assertThat(method.getParameters().isEmpty()).isTrue();
    }

    @Test
    public void testMethodWithOneParameter() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(byte a) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "int");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(1);
        
        checkScalarType(method.getParameters().get(0).getType(), "byte");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("a");
    }

    @Test
    public void testMethodWithMultipleParameters() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(byte a, int b, char c) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        checkScalarType(method.getReturnType(), "int");
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(3);
        
        checkScalarType(method.getParameters().get(0).getType(), "byte");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("a");
        
        checkScalarType(method.getParameters().get(1).getType(), "int");
        assertThat(method.getParameters().get(1).getNameString()).isEqualTo("b");
        
        checkScalarType(method.getParameters().get(2).getType(), "char");
        assertThat(method.getParameters().get(2).getNameString()).isEqualTo("c");
    }

    @Test
    public void testMethodWithReferenceParameters() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(SomeType a, AnotherType b, YetAType c) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        checkScalarType(method.getReturnType(), "int");
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(3);
        
        checkIdentifierType(method.getParameters().get(0).getType(), "SomeType");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("a");
        
        checkIdentifierType(method.getParameters().get(1).getType(), "AnotherType");
        assertThat(method.getParameters().get(1).getNameString()).isEqualTo("b");
        
        checkIdentifierType(method.getParameters().get(2).getType(), "YetAType");
        assertThat(method.getParameters().get(2).getNameString()).isEqualTo("c");
    }

    @Test
    public void testMethodWithScopedReferenceParameters() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(com.test.SomeType a, com.test.AnotherType b, com.test.YetAType c) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "int");
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(3);
        
        checkScopedType(method.getParameters().get(0).getType(), "com", "test", "SomeType");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("a");
        
        checkScopedType(method.getParameters().get(1).getType(), "com", "test", "AnotherType");
        assertThat(method.getParameters().get(1).getNameString()).isEqualTo("b");
        
        checkScopedType(method.getParameters().get(2).getType(), "com", "test", "YetAType");
        assertThat(method.getParameters().get(2).getNameString()).isEqualTo("c");
    }

    @Test
    public void testMethodOneScalarLocalVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement = (VariableDeclarationStatement)method.getBlock().getStatements().get(0);
        checkScalarType(statement.getTypeReference(), "int");
        
        assertThat(statement.getModifiers().isEmpty()).isTrue();
        
        assertThat(statement.getDeclarations().size()).isEqualTo(1);
        assertThat(statement.getDeclarations().get(0).getNameString()).isEqualTo("a");
    }

    @Test
    public void testMethodOneMultipleScalarLocalVariables() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a, b, c; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement = (VariableDeclarationStatement)method.getBlock().getStatements().get(0);
        checkScalarType(statement.getTypeReference(), "int");
        
        assertThat(statement.getModifiers().isEmpty()).isTrue();
        
        assertThat(statement.getDeclarations().size()).isEqualTo(3);
        assertThat(statement.getDeclarations().get(0).getNameString()).isEqualTo("a");
        assertThat(statement.getDeclarations().get(1).getNameString()).isEqualTo("b");
        assertThat(statement.getDeclarations().get(2).getNameString()).isEqualTo("c");
    }

    private static ClassDefinition checkBasicClass(CompilationUnit compilationUnit, String className) {
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        assertThat(classDefinition.getNameString()).isEqualTo(className);
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers().size()).isEqualTo(1);
        
        return classDefinition;
    }
    
    private ClassMethod checkBasicMethod(CompilationUnit compilationUnit, String className, String methodName) {
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, className);
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        final ScalarTypeReference returnType = (ScalarTypeReference)method.getReturnType();
        
        assertThat(returnType.getTypeName().getName()).isEqualTo("void");
        assertThat(method.getNameString()).isEqualTo(methodName);
        
        assertThat(method.getParameters().isEmpty()).isTrue();
        
        return method;
    }

    private static void checkScalarType(TypeReference typeReference, String typeName) {

        final ScalarTypeReference type = (ScalarTypeReference)typeReference;
        
        assertThat(type.getTypeName().getName()).isEqualTo(typeName);
    }

    private static void checkIdentifierType(TypeReference typeReference, String typeName) {

        final ResolveLaterTypeReference type = (ResolveLaterTypeReference)typeReference;
        
        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo(typeName);
    }

    private static void checkScopedType(TypeReference typeReference, String ...names) {

        final List<String> parts = Arrays.asList(names);
        
        final ResolveLaterTypeReference type = (ResolveLaterTypeReference)typeReference;
        
        assertThat(type.getScopedName().getScope()).isEqualTo(parts.subList(0, parts.size() - 1));
        assertThat(type.getScopedName().getName()).isEqualTo(parts.get(parts.size() - 1));
    }
}
