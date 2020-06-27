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
import com.neaterbits.compiler.ast.objects.expression.AssignmentExpression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionList;
import com.neaterbits.compiler.ast.objects.expression.FieldAccess;
import com.neaterbits.compiler.ast.objects.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.objects.expression.PrimaryList;
import com.neaterbits.compiler.ast.objects.expression.UnresolvedMethodInvocationExpression;
import com.neaterbits.compiler.ast.objects.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.statement.AssignmentStatement;
import com.neaterbits.compiler.ast.objects.statement.ConditionBlock;
import com.neaterbits.compiler.ast.objects.statement.ExpressionStatement;
import com.neaterbits.compiler.ast.objects.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.ast.objects.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.ast.objects.statement.WhileStatement;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.objects.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.ScalarTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.objects.variables.NameReference;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.parse.FieldAccessType;
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
    public void testParseMarkerAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        assertThat(classDefinition.getModifiers().getAnnotations().get(0).getScopedName().getScope()).isNull();
        assertThat(classDefinition.getModifiers().getAnnotations().get(0).getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(classDefinition.getModifiers().getAnnotations().get(0).getElements().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
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

    @Test
    public void testMethodOneIdentifierLocalVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { SomeType a; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement = (VariableDeclarationStatement)method.getBlock().getStatements().get(0);
        checkIdentifierType(statement.getTypeReference(), "SomeType");
        
        assertThat(statement.getModifiers().isEmpty()).isTrue();
        
        assertThat(statement.getDeclarations().size()).isEqualTo(1);
        assertThat(statement.getDeclarations().get(0).getNameString()).isEqualTo("a");
    }

    @Test
    public void testMethodOneScopedLocalVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { com.test.SomeType a; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement = (VariableDeclarationStatement)method.getBlock().getStatements().get(0);
        checkScopedType(statement.getTypeReference(), "com", "test", "SomeType");
        
        assertThat(statement.getModifiers().isEmpty()).isTrue();
        
        assertThat(statement.getDeclarations().size()).isEqualTo(1);
        assertThat(statement.getDeclarations().get(0).getNameString()).isEqualTo("a");
    }

    @Test
    public void testIfStatement() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; if (a == 1) { } } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(2);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a");

        final IfElseIfElseStatement ifStatement = (IfElseIfElseStatement)method.getBlock().getStatements().get(1);
        assertThat(ifStatement.getConditions().size()).isEqualTo(1);

        checkVarLiteralCondition(ifStatement.getConditions().get(0), Relational.EQUALS);
    }

    @Test
    public void testOneLineIfStatement() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; if (a == 1) ; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(2);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a");

        final IfElseIfElseStatement ifStatement = (IfElseIfElseStatement)method.getBlock().getStatements().get(1);
        assertThat(ifStatement.getConditions().size()).isEqualTo(1);

        checkVarLiteralCondition(ifStatement.getConditions().get(0), Relational.EQUALS);
    }

    @Test
    public void testIfElseStatement() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; if (a == 1) { } else { } } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(2);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a");

        final IfElseIfElseStatement ifStatement = (IfElseIfElseStatement)method.getBlock().getStatements().get(1);
        assertThat(ifStatement.getConditions().size()).isEqualTo(1);
        
        checkVarLiteralCondition(ifStatement.getConditions().get(0), Relational.EQUALS);
        
        assertThat(ifStatement.getElseBlock()).isNotNull();
        assertThat(ifStatement.getElseBlock().getStatements().isEmpty()).isTrue();
    }
    
    @Test
    public void testIfElseIfElseStatement() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; if (a == 1) { } else if (a == 2) { } else  if (a == 3) { } else { } } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(2);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a");

        final IfElseIfElseStatement ifStatement = (IfElseIfElseStatement)method.getBlock().getStatements().get(1);
        assertThat(ifStatement.getConditions().size()).isEqualTo(3);
        
        checkVarLiteralCondition(ifStatement.getConditions().get(0), Relational.EQUALS);
        checkVarLiteralCondition(ifStatement.getConditions().get(1), Relational.EQUALS);
        checkVarLiteralCondition(ifStatement.getConditions().get(2), Relational.EQUALS);
        
        assertThat(ifStatement.getElseBlock()).isNotNull();
        assertThat(ifStatement.getElseBlock().getStatements().isEmpty()).isTrue();
    }

    @Test
    public void testIfElseIfWithoutElseStatement() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; if (a == 1) { } else if (a == 2) { } else  if (a == 3) { } } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(2);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a");

        final IfElseIfElseStatement ifStatement = (IfElseIfElseStatement)method.getBlock().getStatements().get(1);
        assertThat(ifStatement.getConditions().size()).isEqualTo(3);
        
        checkVarLiteralCondition(ifStatement.getConditions().get(0), Relational.EQUALS);
        checkVarLiteralCondition(ifStatement.getConditions().get(1), Relational.EQUALS);
        checkVarLiteralCondition(ifStatement.getConditions().get(2), Relational.EQUALS);
        
        assertThat(ifStatement.getElseBlock()).isNull();
    }

    @Test
    public void testWhileStatement() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; while (a == 1) { } } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(2);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a");

        final WhileStatement whileStatement = (WhileStatement)method.getBlock().getStatements().get(1);
        assertThat(whileStatement.getCondition()).isNotNull();
        
        checkVarLiteralCondition((ExpressionList)whileStatement.getCondition(), Relational.EQUALS);
        
        assertThat(whileStatement.getBlock()).isNotNull();
        assertThat(whileStatement.getBlock().getStatements().isEmpty()).isTrue();
    }

    @Test
    public void testLocalVariableInitializer() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a = 1; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a", 1);
    }

    @Test
    public void testLocalVariableCommaInitializer() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a, b = 0; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement
            = checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", 2);
        
        checkScalarVariableDeclaration(statement, 0, "a", null);
        checkScalarVariableDeclaration(statement, 1, "b", 0);
    }

    @Test
    public void testLocalVariableInitializerCommaVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a = 1, b; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement = checkScalarVariableDeclarationStatement(
                    method.getBlock().getStatements().get(0),
                    "int",
                    2);
        
        checkScalarVariableDeclaration(statement, 0, "a", 1);
        checkScalarVariableDeclaration(statement, 1, "b", null);
    }

    @Test
    public void testLocalVariableInitializerCommaVariableInitializer() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a = 1, b = 0; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement = checkScalarVariableDeclarationStatement(
                method.getBlock().getStatements().get(0),
                "int",
                2);
        
        checkScalarVariableDeclaration(statement, 0, "a", 1);
        checkScalarVariableDeclaration(statement, 1, "b", 0);
    }

    @Test
    public void testAssignmentStatement() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a; a = 1; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(2);

        checkScalarVariableDeclarationStatement(method.getBlock().getStatements().get(0), "int", "a");
        
        final AssignmentStatement assignmentStatement = (AssignmentStatement)method.getBlock().getStatements().get(1);
        final AssignmentExpression assignmentExpression = assignmentStatement.getExpression();
        
        final IntegerLiteral expression = (IntegerLiteral)assignmentExpression.getExpression();
        assertThat(expression).isNotNull();
        assertThat(expression.getValue()).isEqualTo(1L);
    }
    
    @Test
    public void testSameInstanceMethodInvocation() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { callAMethod(); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(0);
        
        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)expressionStatement.getExpression();
        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().isEmpty()).isTrue();
    }

    @Test
    public void testMethodParameters() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { callAMethod(a, b + 3); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(0);
        
        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)expressionStatement.getExpression();
        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().size()).isEqualTo(2);
        
        final NameReference reference = (NameReference)methodInvocation.getParameters().getList().get(0);
        assertThat(reference.getName()).isEqualTo("a");

        final ExpressionList expressionList = (ExpressionList)methodInvocation.getParameters().getList().get(1);
        
        assertThat(expressionList.getExpressions().size()).isEqualTo(2);
        final NameReference bRef = (NameReference)expressionList.getExpressions().get(0);
        assertThat(bRef.getName()).isEqualTo("b");
        final IntegerLiteral literal = (IntegerLiteral)expressionList.getExpressions().get(1);
        assertThat(literal.getValue()).isEqualTo(3);
    }

    @Test
    public void testMethodInvocationWithFieldAccessOfResult() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { callAMethod().someField; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(0);
        
        final PrimaryList primaryList = (PrimaryList)expressionStatement.getExpression();
        assertThat(primaryList.getPrimaries().size()).isEqualTo(2);
        
        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(0);
        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().isEmpty()).isTrue();
        
        final FieldAccess fieldAccess = (FieldAccess)primaryList.getPrimaries().get(1);
        assertThat(fieldAccess.getFieldAccessType()).isEqualTo(FieldAccessType.FIELD);
        assertThat(fieldAccess.getFieldName().getName()).isEqualTo("someField");
    }

    @Test
    public void testMethodInvocationWithFieldAccessAndMethodOfResult() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { callAMethod().someField.callAnotherMethod(); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(0);
        
        final PrimaryList primaryList = (PrimaryList)expressionStatement.getExpression();
        assertThat(primaryList.getPrimaries().size()).isEqualTo(3);
        
        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(0);
        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().isEmpty()).isTrue();
        
        final FieldAccess fieldAccess = (FieldAccess)primaryList.getPrimaries().get(1);
        assertThat(fieldAccess.getFieldAccessType()).isEqualTo(FieldAccessType.FIELD);
        assertThat(fieldAccess.getFieldName().getName()).isEqualTo("someField");

        final MethodInvocationExpression anotherMethodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(2);
        assertThat(anotherMethodInvocation.getCallable().getName()).isEqualTo("callAnotherMethod");
        assertThat(anotherMethodInvocation.getParameters().getList().isEmpty()).isTrue();
    }

    @Test
    public void testMethodInvocationWithMethodInvocationOfResult() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { callAMethod().callAnotherMethod(); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(0);
        
        final PrimaryList primaryList = (PrimaryList)expressionStatement.getExpression();
        assertThat(primaryList.getPrimaries().size()).isEqualTo(2);
        
        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(0);
        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().isEmpty()).isTrue();
        
        final MethodInvocationExpression anotherMethodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(1);
        assertThat(anotherMethodInvocation.getCallable().getName()).isEqualTo("callAnotherMethod");
        assertThat(anotherMethodInvocation.getParameters().getList().isEmpty()).isTrue();
    }

    @Test
    public void testArithmeticOperator() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a = 1, b = a + 3; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                checkScalarVariableDeclarationStatement(
                        method.getBlock().getStatements().get(0),
                        "int",
                        2);

        checkScalarVariableDeclaration(declarationStatement, 0, "a", 1);
        
        final IntegerLiteral aInitializer = (IntegerLiteral)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(aInitializer.getValue()).isEqualTo(1L);
        
        assertThat(declarationStatement.getDeclarations().get(1).getNameString()).isEqualTo("b");
        final ExpressionList bInitializer = (ExpressionList)declarationStatement.getDeclarations().get(1).getInitializer();
        assertThat(bInitializer.getExpressions().size()).isEqualTo(2);
        final NameReference bInitializer1 = (NameReference)bInitializer.getExpressions().get(0);
        assertThat(bInitializer1.getName()).isEqualTo("a");
        final IntegerLiteral bInitializer2 = (IntegerLiteral)bInitializer.getExpressions().get(1);
        assertThat(bInitializer2.getValue()).isEqualTo(3L);
        assertThat(bInitializer.getOperators().size()).isEqualTo(1);
        assertThat(bInitializer.getOperators().get(0)).isEqualTo(Arithmetic.PLUS);
    }

    @Test
    public void testMultipleArithmeticOperators() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a = 1 + 2 - 3 * 6 / 5 % 4; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                checkScalarVariableDeclarationStatement(
                        method.getBlock().getStatements().get(0),
                        "int",
                        1);

        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("a");
        
        final ExpressionList bInitializer = (ExpressionList)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(bInitializer.getExpressions().size()).isEqualTo(6);
        
        checkExpressionListLiteral(bInitializer, 0, 1);
        checkExpressionListLiteral(bInitializer, 1, 2);
        checkExpressionListLiteral(bInitializer, 2, 3);
        checkExpressionListLiteral(bInitializer, 3, 6);
        checkExpressionListLiteral(bInitializer, 4, 5);
        checkExpressionListLiteral(bInitializer, 5, 4);
        
        assertThat(bInitializer.getOperators().size()).isEqualTo(5);
        assertThat(bInitializer.getOperators().get(0)).isEqualTo(Arithmetic.PLUS);
        assertThat(bInitializer.getOperators().get(1)).isEqualTo(Arithmetic.MINUS);
        assertThat(bInitializer.getOperators().get(2)).isEqualTo(Arithmetic.MULTIPLY);
        assertThat(bInitializer.getOperators().get(3)).isEqualTo(Arithmetic.DIVIDE);
        assertThat(bInitializer.getOperators().get(4)).isEqualTo(Arithmetic.MODULUS);
    }

    @Test
    public void testClassStaticOrStaticVarMethodCall() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { SomeClass.callAMethod(); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(0);
        
        final UnresolvedMethodInvocationExpression methodInvocation = (UnresolvedMethodInvocationExpression)expressionStatement.getExpression();
        
        // Class or static var, does not now which until has resolved classes and variables
        
        // System.out.println("## names " + methodInvocation.getNameList().getNames());
        
        assertThat(methodInvocation.getNameList().getNames().size()).isEqualTo(2);

        /*
        assertThat(methodInvocation.getNameList().getNames().get(0)).isEqualTo("SomeClass");
        assertThat(methodInvocation.getNameList().getNames().get(1)).isEqualTo("callAMethod");
        */

        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().isEmpty()).isTrue();

        assertThat(methodInvocation.getInvocationType()).isEqualTo(MethodInvocationType.NAMED_CLASS_STATIC_OR_STATIC_VAR);
    }
    
    private void checkExpressionListLiteral(ExpressionList list, int index, int value) {
        
        final IntegerLiteral literal = (IntegerLiteral)list.getExpressions().get(index);
        
        assertThat(literal.getValue()).isEqualTo(value);
        
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

    private VariableDeclarationStatement checkScalarVariableDeclarationStatement(
            Statement statement,
            String scalarType,
            String varName) {
        
        return checkScalarVariableDeclarationStatement(statement, scalarType, varName, null);
    }

    private VariableDeclarationStatement checkScalarVariableDeclarationStatement(
            Statement statement,
            String scalarType,
            String varName,
            Integer value) {
        
        final VariableDeclarationStatement declarationStatement = checkScalarVariableDeclarationStatement(statement, scalarType, 1);
        
        checkScalarVariableDeclaration(declarationStatement, 0, varName, value);
        
        return declarationStatement;
    }

    private VariableDeclarationStatement checkScalarVariableDeclarationStatement(
            Statement statement,
            String scalarType,
            int num) {
        
        final VariableDeclarationStatement declarationStatement = (VariableDeclarationStatement)statement;
        
        checkScalarType(declarationStatement.getTypeReference(), scalarType);
        assertThat(declarationStatement.getModifiers().isEmpty()).isTrue();
        assertThat(declarationStatement.getDeclarations().size()).isEqualTo(num);
        
        return declarationStatement;
    }

    private void checkScalarVariableDeclaration(
            VariableDeclarationStatement declarationStatement,
            int index,
            String varName,
            Integer value) {
        
        assertThat(declarationStatement.getDeclarations().get(index).getNameString()).isEqualTo(varName);
        
        if (value != null ) {
            final IntegerLiteral integerLiteral = (IntegerLiteral)declarationStatement.getDeclarations().get(index).getInitializer();
            
            assertThat(integerLiteral.getValue()).isEqualTo((long)value);
        }
        else {
            assertThat(declarationStatement.getDeclarations().get(index).getInitializer()).isNull();
        }
    }

    private void checkVarLiteralCondition(ConditionBlock conditionBlock, Relational operator) {
        final ExpressionList expressionList = (ExpressionList)conditionBlock.getCondition();

        checkVarLiteralCondition(expressionList, operator);

        assertThat(conditionBlock.getBlock().getStatements().isEmpty()).isTrue();
    }

    private void checkVarLiteralCondition(ExpressionList expressionList, Relational operator) {
        
        assertThat(expressionList.getExpressions().size()).isEqualTo(2);
        assertThat(expressionList.getOperators().size()).isEqualTo(1);
        assertThat(expressionList.getOperators().get(0)).isEqualTo(operator);
    }
}
