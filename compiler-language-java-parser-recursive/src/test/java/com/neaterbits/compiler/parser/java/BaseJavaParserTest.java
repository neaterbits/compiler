package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationCode;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Import;
import com.neaterbits.compiler.ast.objects.Namespace;

public abstract class BaseJavaParserTest {

    static {
        BaseASTElement.REQUIRE_CONTEXT = false;
    }

    abstract CompilationUnit parse(String source) throws IOException, ParseException;

    @Test
    public void testParseNamespace() throws IOException, ParseException {
        
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
    public void testParseClassImport() throws IOException, ParseException {
        
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
    public void testParsePackageOnDemandImport() throws IOException, ParseException {
        
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
    public void testParseClassOnDemandImport() throws IOException, ParseException {
        
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
    public void testParseStaticOnDemandStaticImport() throws IOException, ParseException {
        
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
    public void testParseStaticMethodStaticImport() throws IOException, ParseException {
        
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

}
