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
import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.ast.objects.annotation.AnnotationElement;
import com.neaterbits.compiler.ast.objects.block.ClassMethod;
import com.neaterbits.compiler.ast.objects.block.Constructor;
import com.neaterbits.compiler.ast.objects.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionList;
import com.neaterbits.compiler.ast.objects.expression.FieldAccess;
import com.neaterbits.compiler.ast.objects.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.objects.expression.NestedExpression;
import com.neaterbits.compiler.ast.objects.expression.ParameterList;
import com.neaterbits.compiler.ast.objects.expression.PrimaryList;
import com.neaterbits.compiler.ast.objects.expression.UnaryExpression;
import com.neaterbits.compiler.ast.objects.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.ast.objects.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.ast.objects.expression.literal.NamePrimary;
import com.neaterbits.compiler.ast.objects.expression.literal.StringLiteral;
import com.neaterbits.compiler.ast.objects.generics.NamedTypeArgument;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.statement.ConditionBlock;
import com.neaterbits.compiler.ast.objects.statement.ExpressionStatement;
import com.neaterbits.compiler.ast.objects.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.ast.objects.statement.IteratorForStatement;
import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.ast.objects.statement.ThrowStatement;
import com.neaterbits.compiler.ast.objects.statement.VariableDeclarationStatement;
import com.neaterbits.compiler.ast.objects.statement.WhileStatement;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ConstructorMember;
import com.neaterbits.compiler.ast.objects.typedefinition.EnumDefinition;
import com.neaterbits.compiler.ast.objects.typereference.UnresolvedTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.ScalarTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import com.neaterbits.compiler.ast.objects.variables.NameReference;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.Visibility;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.IncrementDecrement;
import com.neaterbits.compiler.util.operator.Logical;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.statement.ASTMutability;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.FieldStatic;
import com.neaterbits.compiler.util.typedefinition.FieldVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;
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
    public void testParseNamedGenericClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass<TYPE> { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        
        assertThat(classDefinition.getGenericTypes().size()).isEqualTo(1);
        
        final NamedTypeArgument namedTypeArgument = (NamedTypeArgument)classDefinition.getGenericTypes().get(0);
        assertThat(namedTypeArgument.getNameString()).isEqualTo("TYPE");
        assertThat(namedTypeArgument.getTypeBounds().isEmpty()).isTrue();
        
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseNamedGenericWithBoundClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass<TYPE extends BaseClass> { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        
        assertThat(classDefinition.getGenericTypes().size()).isEqualTo(1);
        
        final NamedTypeArgument namedTypeArgument = (NamedTypeArgument)classDefinition.getGenericTypes().get(0);
        assertThat(namedTypeArgument.getNameString()).isEqualTo("TYPE");
        assertThat(namedTypeArgument.getTypeBounds().size()).isEqualTo(1);
        assertThat(namedTypeArgument.getTypeBounds().get(0).getType()).isEqualTo(TypeBoundType.EXTENDS);

        final UnresolvedTypeReference typeReference = (UnresolvedTypeReference)namedTypeArgument.getTypeBounds().get(0).getTypeReference();

        assertThat(typeReference.getScopedName().getScope()).isNull();
        assertThat(typeReference.getScopedName().getName()).isEqualTo("BaseClass");
        
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseNamedGenericWithGenericBoundClass() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass<TYPE extends BaseClass<String>> { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().isEmpty()).isTrue();
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        
        assertThat(classDefinition.getGenericTypes().size()).isEqualTo(1);
        
        final NamedTypeArgument namedTypeArgument = (NamedTypeArgument)classDefinition.getGenericTypes().get(0);
        assertThat(namedTypeArgument.getNameString()).isEqualTo("TYPE");
        assertThat(namedTypeArgument.getTypeBounds().size()).isEqualTo(1);
        assertThat(namedTypeArgument.getTypeBounds().get(0).getType()).isEqualTo(TypeBoundType.EXTENDS);
        
        final UnresolvedTypeReference typeReference = (UnresolvedTypeReference)namedTypeArgument.getTypeBounds().get(0).getTypeReference();
        
        assertThat(typeReference.getScopedName().getScope()).isNull();
        assertThat(typeReference.getScopedName().getName()).isEqualTo("BaseClass");
        
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseEnum() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "enum TestEnum { VALUE, ANOTHER_VALUE, YET_ANOTHER; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final EnumDefinition enumDefinition = (EnumDefinition)compilationUnit.getCode().get(1);
        
        assertThat(enumDefinition.getModifiers().isEmpty()).isTrue();

        assertThat(enumDefinition.getNameString()).isEqualTo("TestEnum");
        assertThat(enumDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(enumDefinition.getConstants().size()).isEqualTo(3);
        assertThat(enumDefinition.getConstants().get(0).getNameString()).isEqualTo("VALUE");
        assertThat(enumDefinition.getConstants().get(1).getNameString()).isEqualTo("ANOTHER_VALUE");
        assertThat(enumDefinition.getConstants().get(2).getNameString()).isEqualTo("YET_ANOTHER");
        assertThat(enumDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseEnumWithParameterValues() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "enum TestEnum { VALUE(1), ANOTHER_VALUE(\"string\", false), YET_ANOTHER(345); }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final EnumDefinition enumDefinition = (EnumDefinition)compilationUnit.getCode().get(1);
        
        assertThat(enumDefinition.getModifiers().isEmpty()).isTrue();

        assertThat(enumDefinition.getNameString()).isEqualTo("TestEnum");
        assertThat(enumDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(enumDefinition.getConstants().size()).isEqualTo(3);
        assertThat(enumDefinition.getConstants().get(0).getNameString()).isEqualTo("VALUE");
        
        assertThat(enumDefinition.getConstants().get(0).getNameString()).isEqualTo("VALUE");
        
        final ParameterList valueParameters = enumDefinition.getConstants().get(0).getParameters();
        assertThat(valueParameters.getList().size()).isEqualTo(1);
        
        final IntegerLiteral valueIntegerLiteral = (IntegerLiteral)valueParameters.getList().get(0);
        assertThat(valueIntegerLiteral.getValue()).isEqualTo(1);
        
        assertThat(enumDefinition.getConstants().get(1).getNameString()).isEqualTo("ANOTHER_VALUE");
        
        final ParameterList anotherValueParameters = enumDefinition.getConstants().get(1).getParameters();
        assertThat(anotherValueParameters.getList().size()).isEqualTo(2);
        
        final StringLiteral anotherValueStringLiteral = (StringLiteral)anotherValueParameters.getList().get(0);
        assertThat(anotherValueStringLiteral.getValue()).isEqualTo("string");

        final BooleanLiteral anotherValueBooleanLiteral = (BooleanLiteral)anotherValueParameters.getList().get(1);
        assertThat(anotherValueBooleanLiteral.getValue()).isEqualTo(false);

        final ParameterList yetAnotherValueParameters = enumDefinition.getConstants().get(2).getParameters();
        assertThat(yetAnotherValueParameters.getList().size()).isEqualTo(1);

        assertThat(enumDefinition.getConstants().get(2).getNameString()).isEqualTo("YET_ANOTHER");
        assertThat(yetAnotherValueParameters.getList().size()).isEqualTo(1);
        
        final IntegerLiteral yetAnotherValueIntegerLiteral = (IntegerLiteral)yetAnotherValueParameters.getList().get(0);
        assertThat(yetAnotherValueIntegerLiteral.getValue()).isEqualTo(345);

        assertThat(enumDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseEnumImplementsOneInterface() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "enum TestEnum implements SomeInterface { VALUE, ANOTHER_VALUE, YET_ANOTHER; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final EnumDefinition enumDefinition = (EnumDefinition)compilationUnit.getCode().get(1);
        
        assertThat(enumDefinition.getModifiers().isEmpty()).isTrue();

        assertThat(enumDefinition.getNameString()).isEqualTo("TestEnum");
        
        assertThat(enumDefinition.getImplementsInterfaces().size()).isEqualTo(1);
        
        final UnresolvedTypeReference type = (UnresolvedTypeReference)enumDefinition.getImplementsInterfaces().get(0);
        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo("SomeInterface");
        
        assertThat(enumDefinition.getConstants().size()).isEqualTo(3);
        assertThat(enumDefinition.getConstants().get(0).getNameString()).isEqualTo("VALUE");
        assertThat(enumDefinition.getConstants().get(1).getNameString()).isEqualTo("ANOTHER_VALUE");
        assertThat(enumDefinition.getConstants().get(2).getNameString()).isEqualTo("YET_ANOTHER");
        assertThat(enumDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseEnumImplementsMultiple() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "enum TestEnum implements SomeInterface, com.test.AnotherInterface { VALUE, ANOTHER_VALUE, YET_ANOTHER; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final EnumDefinition enumDefinition = (EnumDefinition)compilationUnit.getCode().get(1);
        
        assertThat(enumDefinition.getModifiers().isEmpty()).isTrue();

        assertThat(enumDefinition.getNameString()).isEqualTo("TestEnum");
        
        assertThat(enumDefinition.getImplementsInterfaces().size()).isEqualTo(2);

        UnresolvedTypeReference type = (UnresolvedTypeReference)enumDefinition.getImplementsInterfaces().get(0);
        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo("SomeInterface");
        
        type = (UnresolvedTypeReference)enumDefinition.getImplementsInterfaces().get(1);
        assertThat(type.getScopedName().getScope()).isEqualTo(Arrays.asList("com", "test"));
        assertThat(type.getScopedName().getName()).isEqualTo("AnotherInterface");
        
        assertThat(enumDefinition.getConstants().size()).isEqualTo(3);
        assertThat(enumDefinition.getConstants().get(0).getNameString()).isEqualTo("VALUE");
        assertThat(enumDefinition.getConstants().get(1).getNameString()).isEqualTo("ANOTHER_VALUE");
        assertThat(enumDefinition.getConstants().get(2).getNameString()).isEqualTo("YET_ANOTHER");
        assertThat(enumDefinition.getMembers()).isEmpty();
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
    public void testParseLiteralAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation(123) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(1);
        
        final AnnotationElement annotationElement = annotation.getElements().get(0);
        
        assertThat(annotationElement.getName()).isNull();
        assertThat(annotationElement.getAnnotation()).isNull();
        assertThat(annotationElement.getValueList()).isNull();

        final IntegerLiteral integerLiteral = (IntegerLiteral)annotationElement.getExpression();
        assertThat(integerLiteral.getValue()).isEqualTo(123);
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseIdentifierAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation(STATIC_FINAL_VAR) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(1);
        
        final AnnotationElement annotationElement = annotation.getElements().get(0);
        
        assertThat(annotationElement.getName()).isNull();
        assertThat(annotationElement.getAnnotation()).isNull();
        assertThat(annotationElement.getValueList()).isNull();
        
        final NameReference nameReference = (NameReference)annotationElement.getExpression();
        assertThat(nameReference.getName()).isEqualTo("STATIC_FINAL_VAR");
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseFinalClassLiteralAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "final @TheAnnotation(123) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        assertThat(classDefinition.getModifiers().getModifier(Subclassing.class)).isEqualTo(Subclassing.FINAL);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(1);
        
        final AnnotationElement annotationElement = annotation.getElements().get(0);
        
        assertThat(annotationElement.getName()).isNull();
        assertThat(annotationElement.getAnnotation()).isNull();
        assertThat(annotationElement.getValueList()).isNull();

        final IntegerLiteral integerLiteral = (IntegerLiteral)annotationElement.getExpression();
        assertThat(integerLiteral.getValue()).isEqualTo(123);
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseNameValueAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation(name=123) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(1);
        
        final AnnotationElement annotationElement = annotation.getElements().get(0);
        
        assertThat(annotationElement.getName()).isEqualTo("name");
        assertThat(annotationElement.getAnnotation()).isNull();
        assertThat(annotationElement.getValueList()).isNull();

        final IntegerLiteral integerLiteral = (IntegerLiteral)annotationElement.getExpression();
        assertThat(integerLiteral.getValue()).isEqualTo(123);
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseNameValuesAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation(name=123, otherName=456) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(2);
        
        final AnnotationElement annotationElement1 = annotation.getElements().get(0);
        
        assertThat(annotationElement1.getName()).isEqualTo("name");
        assertThat(annotationElement1.getAnnotation()).isNull();
        assertThat(annotationElement1.getValueList()).isNull();

        final IntegerLiteral integerLiteral1 = (IntegerLiteral)annotationElement1.getExpression();
        assertThat(integerLiteral1.getValue()).isEqualTo(123);

        final AnnotationElement annotationElement2 = annotation.getElements().get(1);
        
        assertThat(annotationElement2.getName()).isEqualTo("otherName");
        assertThat(annotationElement2.getAnnotation()).isNull();
        assertThat(annotationElement2.getValueList()).isNull();

        final IntegerLiteral integerLiteral2 = (IntegerLiteral)annotationElement2.getExpression();
        assertThat(integerLiteral2.getValue()).isEqualTo(456);
        
        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseNameAnnotationValuesAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation(name=@ValueAnnotation, otherName=@OtherValueAnnotation) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(2);
        
        final AnnotationElement annotationElement1 = annotation.getElements().get(0);
        
        assertThat(annotationElement1.getExpression()).isNull();
        assertThat(annotationElement1.getName()).isEqualTo("name");
        assertThat(annotationElement1.getAnnotation().getScopedName().getScope()).isNull();
        assertThat(annotationElement1.getAnnotation().getScopedName().getName()).isEqualTo("ValueAnnotation");
        assertThat(annotationElement1.getValueList()).isNull();

        final AnnotationElement annotationElement2 = annotation.getElements().get(1);

        assertThat(annotationElement2.getExpression()).isNull();
        assertThat(annotationElement2.getName()).isEqualTo("otherName");
        assertThat(annotationElement2.getAnnotation().getScopedName().getScope()).isNull();
        assertThat(annotationElement2.getAnnotation().getScopedName().getName()).isEqualTo("OtherValueAnnotation");
        assertThat(annotationElement2.getValueList()).isNull();

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseAnnotationWithAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation(@OtherAnnotation) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(1);
        
        final AnnotationElement annotationElement = annotation.getElements().get(0);
        
        assertThat(annotationElement.getName()).isNull();
        assertThat(annotationElement.getExpression()).isNull();
        assertThat(annotationElement.getAnnotation().getScopedName().getScope()).isNull();
        assertThat(annotationElement.getAnnotation().getScopedName().getName()).isEqualTo("OtherAnnotation");
        assertThat(annotationElement.getValueList()).isNull();

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseLiteralValueListAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation({ 123, 456 }) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(1);
        
        final AnnotationElement annotationElement = annotation.getElements().get(0);
        
        assertThat(annotationElement.getName()).isNull();
        assertThat(annotationElement.getExpression()).isNull();
        assertThat(annotationElement.getAnnotation()).isNull();
        assertThat(annotationElement.getValueList().size()).isEqualTo(2);

        final IntegerLiteral integerLiteral1 = (IntegerLiteral)annotationElement.getValueList().get(0).getExpression();
        assertThat(integerLiteral1.getValue()).isEqualTo(123);

        final IntegerLiteral integerLiteral2 = (IntegerLiteral)annotationElement.getValueList().get(1).getExpression();
        assertThat(integerLiteral2.getValue()).isEqualTo(456);

        assertThat(classDefinition.getNameString()).isEqualTo("TestClass");
        assertThat(classDefinition.getExtendsClasses()).isEmpty();
        assertThat(classDefinition.getImplementsInterfaces()).isEmpty();
        assertThat(classDefinition.getMembers()).isEmpty();
    }

    @Test
    public void testParseAnnotationValueListAnnotation() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "@TheAnnotation(name = { @ValueAnnotation, @OtherValueAnnotation }) class TestClass { }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = (ClassDefinition)compilationUnit.getCode().get(1);
        
        assertThat(classDefinition.getModifiers().getAnnotations().size()).isEqualTo(1);
        
        final Annotation annotation = classDefinition.getModifiers().getAnnotations().get(0);
        assertThat(annotation.getScopedName().getScope()).isNull();
        assertThat(annotation.getScopedName().getName()).isEqualTo("TheAnnotation");
        assertThat(annotation.getElements().size()).isEqualTo(1);
        
        final AnnotationElement annotationElement = annotation.getElements().get(0);
        
        assertThat(annotationElement.getName()).isEqualTo("name");
        assertThat(annotationElement.getExpression()).isNull();
        assertThat(annotationElement.getAnnotation()).isNull();
        assertThat(annotationElement.getValueList().size()).isEqualTo(2);

        assertThat(annotationElement.getValueList().get(0).getAnnotation().getScopedName().getScope()).isNull();
        assertThat(annotationElement.getValueList().get(0).getAnnotation().getScopedName().getName())
                            .isEqualTo("ValueAnnotation");

        assertThat(annotationElement.getValueList().get(1).getAnnotation().getScopedName().getScope()).isNull();
        assertThat(annotationElement.getValueList().get(1).getAnnotation().getScopedName().getName())
                            .isEqualTo("OtherValueAnnotation");

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
    public void testParseStaticInitializerMemberVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { private static final int memberVariable = 123; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassDataFieldMember member = (ClassDataFieldMember)classDefinition.getMembers().get(0);

        assertThat(member.getModifiers().getModifier(FieldStatic.class)).isNotNull();
        assertThat(member.getInitializer(0).getNameString()).isEqualTo("memberVariable");
        
        checkScalarType(member.getType(), "int");

        final IntegerLiteral integerLiteral = (IntegerLiteral)member.getInitializer(0).getInitializer();
        assertThat(integerLiteral.getValue()).isEqualTo(123);
    }

    @Test
    public void testParseModiferScalarClassMemberVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { private final int memberVariable; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassDataFieldMember member = (ClassDataFieldMember)classDefinition.getMembers().get(0);
        
        assertThat(member.getModifiers().getModifier(FieldVisibility.class).getVisibility())
                    .isEqualTo(Visibility.PRIVATE);
        assertThat(member.getModifiers().getModifier(ASTMutability.class).getMutability())
                    .isEqualTo(Mutability.VALUE_OR_REF_IMMUTABLE);
    
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
    public void testParseGenericTypeArgumentClassMemberVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { SomeType<TYPE> memberVariable; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassDataFieldMember member = (ClassDataFieldMember)classDefinition.getMembers().get(0);
    
        assertThat(member.getInitializer(0).getNameString()).isEqualTo("memberVariable");
        
        final UnresolvedTypeReference type = checkIdentifierType(member.getType(), "SomeType");
        
        checkIdentifierType(type.getGenericTypeParameters().get(0), "TYPE");
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
    public void testConstructor() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { TestClass() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ConstructorMember member = (ConstructorMember)classDefinition.getMembers().get(0);
        
        final Constructor constructor = member.getConstructor();
        
        assertThat(constructor.getNameString()).isEqualTo("TestClass");
        
        assertThat(constructor.getParameters().isEmpty()).isTrue();
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
    public void testStaticMethod() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { static void someMethod() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        assertThat(member.getModifiers().count()).isEqualTo(1);
        assertThat(member.getModifiers().getModifier(ClassMethodStatic.class)).isNotNull();
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "void");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        assertThat(method.getParameters().isEmpty()).isTrue();
    }

    @Test
    public void testPrivateMethod() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { private void someMethod() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        assertThat(member.getModifiers().count()).isEqualTo(1);
        
        final ClassMethodVisibility methodVisibility = member.getModifiers().getModifier(ClassMethodVisibility.class);
        assertThat(methodVisibility).isEqualTo(ClassMethodVisibility.PRIVATE);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "void");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        assertThat(method.getParameters().isEmpty()).isTrue();
    }

    @Test
    public void testFinalMethod() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { final void someMethod() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        assertThat(member.getModifiers().count()).isEqualTo(1);
        
        final ClassMethodOverride methodOverride = member.getModifiers().getModifier(ClassMethodOverride.class);
        assertThat(methodOverride).isEqualTo(ClassMethodOverride.FINAL);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "void");
        
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
    public void testMethodWithGenericParameter() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(List<String> a) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "int");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(1);
        
        final UnresolvedTypeReference type = checkIdentifierType(method.getParameters().get(0).getType(), "List");
        
        assertThat(type.getGenericTypeParameters().size()).isEqualTo(1);
        checkIdentifierType(type.getGenericTypeParameters().get(0), "String");
    }

    @Test
    public void testMethodWithScalarVarargsParameter() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(byte ... data) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "int");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(1);
        
        checkScalarType(method.getParameters().get(0).getType(), "byte");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("data");
        assertThat(method.getParameters().get(0).isVarArgs()).isTrue();
    }

    @Test
    public void testMethodWithUserVarargsParameter() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(SomeClass ... data) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "int");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(1);
        
        final UnresolvedTypeReference type = (UnresolvedTypeReference)method.getParameters().get(0).getType();
        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo("SomeClass");
        
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("data");
        assertThat(method.getParameters().get(0).isVarArgs()).isTrue();
    }

    @Test
    public void testMethodWithScopedUserVarargsParameter() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int someMethod(com.test.SomeClass ... data) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        
        final ClassMethod method = member.getMethod();
        
        checkScalarType(method.getReturnType(), "int");
        
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(1);
        
        final UnresolvedTypeReference type = (UnresolvedTypeReference)method.getParameters().get(0).getType();
        assertThat(type.getScopedName().getScope()).isEqualTo(Arrays.asList("com", "test"));
        assertThat(type.getScopedName().getName()).isEqualTo("SomeClass");
        
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("data");
        assertThat(method.getParameters().get(0).isVarArgs()).isTrue();
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
    public void testMethodOneGenericIdentifierLocalVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { SomeType<OtherType> a; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);

        final VariableDeclarationStatement statement = (VariableDeclarationStatement)method.getBlock().getStatements().get(0);
        final UnresolvedTypeReference type = checkIdentifierType(statement.getTypeReference(), "SomeType");
        
        assertThat(type.getGenericTypeParameters().size()).isEqualTo(1);
        checkIdentifierType(type.getGenericTypeParameters().get(0), "OtherType");
        
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
        
        checkIntScalarVariableDeclaration(statement, 0, "a", null);
        checkIntScalarVariableDeclaration(statement, 1, "b", 0);
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
        
        checkIntScalarVariableDeclaration(statement, 0, "a", 1);
        checkIntScalarVariableDeclaration(statement, 1, "b", null);
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
        
        checkIntScalarVariableDeclaration(statement, 0, "a", 1);
        checkIntScalarVariableDeclaration(statement, 1, "b", 0);
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
        
        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(1);
        final ExpressionList expressionList = (ExpressionList)expressionStatement.getExpression();
        
        final IntegerLiteral expression = (IntegerLiteral)expressionList.getExpressions().get(1);

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
    public void testUnresolvedMethodInvocation() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { SomeClass.callAMethod(); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final ExpressionStatement expressionStatement = (ExpressionStatement)method.getBlock().getStatements().get(0);
        
        final PrimaryList primaryList = (PrimaryList)expressionStatement.getExpression();

        final NamePrimary namePrimary = (NamePrimary)primaryList.getPrimaries().get(0);
        assertThat(namePrimary.getName()).isEqualTo("SomeClass");

        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(1);
        
        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().isEmpty()).isTrue();
    }

    @Test
    public void testConditionObjectMethodInvocation() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { boolean value = object.callAMethod(); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement = (VariableDeclarationStatement)method.getBlock().getStatements().get(0);
        
        final PrimaryList primaryList = (PrimaryList)declarationStatement.getDeclarations().get(0).getInitializer();

        assertThat(primaryList.getPrimaries().size()).isEqualTo(2);
        
        final NamePrimary namePrimary = (NamePrimary)primaryList.getPrimaries().get(0);
        assertThat(namePrimary.getName()).isEqualTo("object");

        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(1);
        
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

        checkIntScalarVariableDeclaration(declarationStatement, 0, "a", 1);
        
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
        
        final ExpressionList topLevelInitializer = (ExpressionList)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(topLevelInitializer.getExpressions().size()).isEqualTo(3);
        
        checkExpressionListLiteral(topLevelInitializer, 0, 1);
        checkExpressionListLiteral(topLevelInitializer, 1, 2);
        
        final NestedExpression nestedExpression = (NestedExpression)topLevelInitializer.getExpressions().get(2);
        final ExpressionList subInitializer = (ExpressionList)nestedExpression.getExpression();
        
        assertThat(subInitializer.getExpressions().size()).isEqualTo(4);
        checkExpressionListLiteral(subInitializer, 0, 3);
        checkExpressionListLiteral(subInitializer, 1, 6);
        checkExpressionListLiteral(subInitializer, 2, 5);
        checkExpressionListLiteral(subInitializer, 3, 4);
        
        assertThat(topLevelInitializer.getOperators().size()).isEqualTo(2);
        assertThat(topLevelInitializer.getOperators().get(0)).isEqualTo(Arithmetic.PLUS);
        assertThat(topLevelInitializer.getOperators().get(1)).isEqualTo(Arithmetic.MINUS);
        
        assertThat(subInitializer.getOperators().size()).isEqualTo(3);
        assertThat(subInitializer.getOperators().get(0)).isEqualTo(Arithmetic.MULTIPLY);
        assertThat(subInitializer.getOperators().get(1)).isEqualTo(Arithmetic.DIVIDE);
        assertThat(subInitializer.getOperators().get(2)).isEqualTo(Arithmetic.MODULUS);
    }

    @Test
    public void testArithmeticOperatorsWithLowerPrecedenceLast() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { int a = 6 / 5 % 4 * 1 + 2 - 3; } }";
        
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
        
        final ExpressionList topLevelInitializer = (ExpressionList)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(topLevelInitializer.getExpressions().size()).isEqualTo(3);

        final NestedExpression nestedExpression = (NestedExpression)topLevelInitializer.getExpressions().get(0);
        final ExpressionList subInitializer = (ExpressionList)nestedExpression.getExpression();
        
        assertThat(subInitializer.getExpressions().size()).isEqualTo(4);
        checkExpressionListLiteral(subInitializer, 0, 6);
        checkExpressionListLiteral(subInitializer, 1, 5);
        checkExpressionListLiteral(subInitializer, 2, 4);
        checkExpressionListLiteral(subInitializer, 3, 1);

        checkExpressionListLiteral(topLevelInitializer, 1, 2);
        checkExpressionListLiteral(topLevelInitializer, 2, 3);
        
        assertThat(subInitializer.getOperators().size()).isEqualTo(3);
        assertThat(subInitializer.getOperators().get(0)).isEqualTo(Arithmetic.DIVIDE);
        assertThat(subInitializer.getOperators().get(1)).isEqualTo(Arithmetic.MODULUS);
        assertThat(subInitializer.getOperators().get(2)).isEqualTo(Arithmetic.MULTIPLY);
        
        assertThat(topLevelInitializer.getOperators().size()).isEqualTo(2);
        assertThat(topLevelInitializer.getOperators().get(0)).isEqualTo(Arithmetic.PLUS);
        assertThat(topLevelInitializer.getOperators().get(1)).isEqualTo(Arithmetic.MINUS);
    }

    @Test
    public void testUnaryOperator() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { boolean value = !false; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                checkScalarVariableDeclarationStatement(
                        method.getBlock().getStatements().get(0),
                        "boolean",
                        1);

        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("value");

        final UnaryExpression unaryExpression = (UnaryExpression)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(unaryExpression).isNotNull();
        assertThat(unaryExpression.getOperator()).isEqualTo(Logical.NOT);

        final BooleanLiteral booleanLiteral = (BooleanLiteral)unaryExpression.getExpression();
        assertThat(booleanLiteral.getValue()).isFalse();
        
    }

    @Test
    public void testUnaryOperatorPrecedence() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { boolean value = !false && true; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                checkScalarVariableDeclarationStatement(
                        method.getBlock().getStatements().get(0),
                        "boolean",
                        1);

        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("value");
        
        final ExpressionList expressionList = (ExpressionList)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(expressionList.getExpressions().size()).isEqualTo(2);
        assertThat(expressionList.getOperators().size()).isEqualTo(1);

        final UnaryExpression unaryExpression = (UnaryExpression)expressionList.getExpressions().get(0);
        assertThat(unaryExpression).isNotNull();
        assertThat(unaryExpression.getOperator()).isEqualTo(Logical.NOT);

        final BooleanLiteral notBooleanLiteral = (BooleanLiteral)unaryExpression.getExpression();
        assertThat(notBooleanLiteral.getValue()).isFalse();
        
        assertThat(expressionList.getOperators().get(0)).isEqualTo(Logical.AND);
        
        final BooleanLiteral andBooleanLiteral = (BooleanLiteral)expressionList.getExpressions().get(1);
        assertThat(andBooleanLiteral.getValue()).isTrue();
    }

    @Test
    public void testUnaryOperatorAndUnaryOperator() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { boolean value = !!true; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                checkScalarVariableDeclarationStatement(
                        method.getBlock().getStatements().get(0),
                        "boolean",
                        1);

        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("value");

        final UnaryExpression unaryExpression = (UnaryExpression)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(unaryExpression).isNotNull();
        assertThat(unaryExpression.getOperator()).isEqualTo(Logical.NOT);

        final UnaryExpression otherUnaryExpression = (UnaryExpression)unaryExpression.getExpression();
        assertThat(otherUnaryExpression).isNotNull();
        assertThat(otherUnaryExpression.getOperator()).isEqualTo(Logical.NOT);

        final BooleanLiteral booleanLiteral = (BooleanLiteral)otherUnaryExpression.getExpression();
        assertThat(booleanLiteral.getValue()).isTrue();
        
    }
    

    @Test
    public void testPostfixUnaryOperator() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { boolean value = var ++; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                checkScalarVariableDeclarationStatement(
                        method.getBlock().getStatements().get(0),
                        "boolean",
                        1);

        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("value");

        final UnaryExpression unaryExpression = (UnaryExpression)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(unaryExpression).isNotNull();
        assertThat(unaryExpression.getOperator()).isEqualTo(IncrementDecrement.POST_INCREMENT);

        final NameReference nameReference = (NameReference)unaryExpression.getExpression();
        assertThat(nameReference.getName()).isEqualTo("var");
    }

    @Test
    public void testPrefixUnaryOperator() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { boolean value = ++ var; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                checkScalarVariableDeclarationStatement(
                        method.getBlock().getStatements().get(0),
                        "boolean",
                        1);

        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("value");

        final UnaryExpression unaryExpression = (UnaryExpression)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(unaryExpression).isNotNull();
        assertThat(unaryExpression.getOperator()).isEqualTo(IncrementDecrement.PRE_INCREMENT);

        final NameReference nameReference = (NameReference)unaryExpression.getExpression();
        assertThat(nameReference.getName()).isEqualTo("var");
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

        final PrimaryList primaryList = (PrimaryList)expressionStatement.getExpression();

        final NamePrimary namePrimary = (NamePrimary)primaryList.getPrimaries().get(0);
        
        assertThat(namePrimary.getName()).isEqualTo("SomeClass");

        final MethodInvocationExpression methodInvocation = (MethodInvocationExpression)primaryList.getPrimaries().get(1);
        
        // Class or static var, does not now which until has resolved classes and variables
        
        // System.out.println("## names " + methodInvocation.getNameList().getNames());

        assertThat(methodInvocation.getCallable().getName()).isEqualTo("callAMethod");
        assertThat(methodInvocation.getParameters().getList().isEmpty()).isTrue();

        assertThat(methodInvocation.getInvocationType()).isEqualTo(MethodInvocationType.UNRESOLVED);
    }

    @Test
    public void testThrowStatement() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { throw exception; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final ThrowStatement throwStatement = (ThrowStatement)method.getBlock().getStatements().get(0);
        
        final NameReference nameReference = (NameReference)throwStatement.getExpression();
        assertThat(nameReference.getName()).isEqualTo("exception");
    }

    @Test
    public void testNewOperator() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { SomeClass var = new SomeClass(); } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement =
                        (VariableDeclarationStatement)method.getBlock().getStatements().get(0);
        
        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("var");

        final ClassInstanceCreationExpression creationExpression
            = (ClassInstanceCreationExpression)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(creationExpression).isNotNull();
        
        final UnresolvedTypeReference type = (UnresolvedTypeReference)creationExpression.getTypeReference();

        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo("SomeClass");
    }

    @Test
    public void testStringLiteral() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { String value = \"theLiteral\"; } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final VariableDeclarationStatement declarationStatement
            = (VariableDeclarationStatement)method.getBlock().getStatements().get(0);

        assertThat(declarationStatement.getDeclarations().get(0).getNameString()).isEqualTo("value");

        final StringLiteral stringLiteral = (StringLiteral)declarationStatement.getDeclarations().get(0).getInitializer();
        assertThat(stringLiteral).isNotNull();
        assertThat(stringLiteral.getValue()).isEqualTo("theLiteral");
    }

    @Test
    public void testForCollection() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod() { for (Integer value : list) { } } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(1);
        
        final IteratorForStatement iteratorForStatement
            = (IteratorForStatement)method.getBlock().getStatements().get(0);

        final UnresolvedTypeReference type = (UnresolvedTypeReference)iteratorForStatement.getVariableDeclaration().getTypeReference();
        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo("Integer");
        
        assertThat(iteratorForStatement.getVariableDeclaration().getNameString()).isEqualTo("value");

        final NameReference nameReference = (NameReference)iteratorForStatement.getCollectionExpression();
        assertThat(nameReference).isNotNull();
        assertThat(nameReference.getName()).isEqualTo("list");
    }

    @Test
    public void testMethodAnnotation() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { @AnAnnotation void someMethod() { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
        assertThat(member.getModifiers().getAnnotations().size()).isEqualTo(1);
        assertThat(member.getModifiers().getAnnotations().get(0).getScopedName().getScope()).isNull();
        assertThat(member.getModifiers().getAnnotations().get(0).getScopedName().getName()).isEqualTo("AnAnnotation");

        final ClassMethod method = checkBasicMethod(member, "someMethod");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(0);
    }

    @Test
    public void testMethodParameterAnnotation() throws IOException, ParserException {
     
        final String source = "package com.test;\n"
                
                + "class TestClass { void someMethod(@AnAnnotation int parameter) { } }";
        
        final CompilationUnit compilationUnit = parse(source);
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassMethod method = checkBasicMethod(compilationUnit, "TestClass", "someMethod", 1);
        
        assertThat(method.getParameters().get(0).getModifiers().getAnnotations().size()).isEqualTo(1);
        assertThat(method.getParameters().get(0).getModifiers().getAnnotations().get(0).getScopedName().getScope()).isNull();
        assertThat(method.getParameters().get(0).getModifiers().getAnnotations().get(0).getScopedName().getName()).isEqualTo("AnAnnotation");
        
        assertThat(method.getBlock()).isNotNull();
        assertThat(method.getBlock().getStatements().size()).isEqualTo(0);
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
        
        return checkBasicMethod(compilationUnit, className, methodName, 0);
    }

    private ClassMethod checkBasicMethod(CompilationUnit compilationUnit, String className, String methodName, int numParams) {
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, className);
        
        final ClassMethodMember member = (ClassMethodMember)classDefinition.getMembers().get(0);
     
        return checkBasicMethod(member, methodName, numParams);
    }

    private ClassMethod checkBasicMethod(ClassMethodMember member, String methodName) {
        return checkBasicMethod(member, methodName, 0);
    }
        
    private ClassMethod checkBasicMethod(ClassMethodMember member, String methodName, int numParams) {
        
        final ClassMethod method = member.getMethod();
        final ScalarTypeReference returnType = (ScalarTypeReference)method.getReturnType();
        
        assertThat(returnType.getTypeName().getName()).isEqualTo("void");
        assertThat(method.getNameString()).isEqualTo(methodName);
        
        assertThat(method.getParameters().size()).isEqualTo(numParams);
        
        return method;
    }

    private static void checkScalarType(TypeReference typeReference, String typeName) {

        final ScalarTypeReference type = (ScalarTypeReference)typeReference;
        
        assertThat(type.getTypeName().getName()).isEqualTo(typeName);
    }

    private static UnresolvedTypeReference checkIdentifierType(TypeReference typeReference, String typeName) {

        final UnresolvedTypeReference type = (UnresolvedTypeReference)typeReference;
        
        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo(typeName);
        
        return type;
    }

    private static void checkScopedType(TypeReference typeReference, String ...names) {

        final List<String> parts = Arrays.asList(names);
        
        final UnresolvedTypeReference type = (UnresolvedTypeReference)typeReference;
        
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
        
        checkIntScalarVariableDeclaration(declarationStatement, 0, varName, value);
        
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

    private void checkIntScalarVariableDeclaration(
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
