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
import com.neaterbits.compiler.ast.objects.block.ClassMethod;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.objects.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.ScalarTypeReference;
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

    @Test
    public void testParseScalarClassMemberVariable() throws IOException, ParserException {
        
        final String source = "package com.test;\n"
                
                + "class TestClass { int memberVariable; }";
        
        final CompilationUnit compilationUnit = parse(source);
        
        assertThat(compilationUnit.getCode()).isNotNull();
        
        final ClassDefinition classDefinition = checkBasicClass(compilationUnit, "TestClass");
        
        final ClassDataFieldMember member = (ClassDataFieldMember)classDefinition.getMembers().get(0);
    
        assertThat(member.getInitializer(0).getNameString()).isEqualTo("memberVariable");
        
        final ScalarTypeReference type = (ScalarTypeReference)member.getType(); 
        assertThat(type.getTypeName().getName()).isEqualTo("int");
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
        
        final ResolveLaterTypeReference type = (ResolveLaterTypeReference)member.getType(); 
        assertThat(type.getScopedName().getScope()).isNull();
        assertThat(type.getScopedName().getName()).isEqualTo("SomeType");
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
        
        assertThat(initializers.get(0).getNameString()).isEqualTo("a");
        ScalarTypeReference type = (ScalarTypeReference)member.getType(); 
        assertThat(type.getTypeName().getName()).isEqualTo("int");
        
        assertThat(initializers.get(1).getNameString()).isEqualTo("b");
        type = (ScalarTypeReference)member.getType(); 
        assertThat(type.getTypeName().getName()).isEqualTo("int");
        
        assertThat(initializers.get(2).getNameString()).isEqualTo("c");
        type = (ScalarTypeReference)member.getType(); 
        assertThat(type.getTypeName().getName()).isEqualTo("int");
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
        final ScalarTypeReference returnType = (ScalarTypeReference)method.getReturnType();
        
        assertThat(returnType.getTypeName().getName()).isEqualTo("int");
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
        final ResolveLaterTypeReference returnType = (ResolveLaterTypeReference)method.getReturnType();
        
        assertThat(returnType.getScopedName().getScope()).isNull();
        assertThat(returnType.getScopedName().getName()).isEqualTo("SomeType");
        
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
        final ResolveLaterTypeReference returnType = (ResolveLaterTypeReference)method.getReturnType();
        
        assertThat(returnType.getScopedName().getScope())
            .isEqualTo(Arrays.asList("com", "test"));
        
        assertThat(returnType.getScopedName().getName()).isEqualTo("SomeType");
        
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
        final ScalarTypeReference returnType = (ScalarTypeReference)method.getReturnType();
        
        assertThat(returnType.getTypeName().getName()).isEqualTo("int");
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(1);
        
        final ScalarTypeReference paramType = (ScalarTypeReference)method.getParameters().get(0).getType();
        assertThat(paramType.getTypeName().getName()).isEqualTo("byte");
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
        final ScalarTypeReference returnType = (ScalarTypeReference)method.getReturnType();
        
        assertThat(returnType.getTypeName().getName()).isEqualTo("int");
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(3);
        
        ScalarTypeReference paramType = (ScalarTypeReference)method.getParameters().get(0).getType();
        assertThat(paramType.getTypeName().getName()).isEqualTo("byte");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("a");
        
        paramType = (ScalarTypeReference)method.getParameters().get(1).getType();
        assertThat(paramType.getTypeName().getName()).isEqualTo("int");
        assertThat(method.getParameters().get(1).getNameString()).isEqualTo("b");
        
        paramType = (ScalarTypeReference)method.getParameters().get(2).getType();
        assertThat(paramType.getTypeName().getName()).isEqualTo("char");
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
        final ScalarTypeReference returnType = (ScalarTypeReference)method.getReturnType();
        
        assertThat(returnType.getTypeName().getName()).isEqualTo("int");
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(3);
        
        ResolveLaterTypeReference paramType = (ResolveLaterTypeReference)method.getParameters().get(0).getType();
        assertThat(paramType.getScopedName().getScope()).isNull();
        assertThat(paramType.getScopedName().getName()).isEqualTo("SomeType");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("a");
        
        paramType = (ResolveLaterTypeReference)method.getParameters().get(1).getType();
        assertThat(paramType.getScopedName().getScope()).isNull();
        assertThat(paramType.getScopedName().getName()).isEqualTo("AnotherType");
        assertThat(method.getParameters().get(1).getNameString()).isEqualTo("b");
        
        paramType = (ResolveLaterTypeReference)method.getParameters().get(2).getType();
        assertThat(paramType.getScopedName().getScope()).isNull();
        assertThat(paramType.getScopedName().getName()).isEqualTo("YetAType");
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
        final ScalarTypeReference returnType = (ScalarTypeReference)method.getReturnType();
        
        assertThat(returnType.getTypeName().getName()).isEqualTo("int");
        assertThat(method.getNameString()).isEqualTo("someMethod");
        
        assertThat(method.getParameters().size()).isEqualTo(3);
        
        ResolveLaterTypeReference paramType = (ResolveLaterTypeReference)method.getParameters().get(0).getType();
        assertThat(paramType.getScopedName().getScope()).isEqualTo(Arrays.asList("com", "test"));
        assertThat(paramType.getScopedName().getName()).isEqualTo("SomeType");
        assertThat(method.getParameters().get(0).getNameString()).isEqualTo("a");
        
        paramType = (ResolveLaterTypeReference)method.getParameters().get(1).getType();
        assertThat(paramType.getScopedName().getScope()).isEqualTo(Arrays.asList("com", "test"));
        assertThat(paramType.getScopedName().getName()).isEqualTo("AnotherType");
        assertThat(method.getParameters().get(1).getNameString()).isEqualTo("b");
        
        paramType = (ResolveLaterTypeReference)method.getParameters().get(2).getType();
        assertThat(paramType.getScopedName().getScope()).isEqualTo(Arrays.asList("com", "test"));
        assertThat(paramType.getScopedName().getName()).isEqualTo("YetAType");
        assertThat(method.getParameters().get(2).getNameString()).isEqualTo("c");
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
}
