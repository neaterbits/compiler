package dev.nimbler.language.codemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import dev.nimbler.language.common.types.TypeName;

public class NameToTypeNoMapTest {

    @Test
    public void testAddMapping() {

        final NameToTypeNoMap map = new NameToTypeNoMap();

        final int typeNo = 1;

        final TypeName typeName = new TypeName(
                new String [] { "com", "test" },
                null,
                "TestType");

        map.addMapping(typeName, typeNo);

        assertThat(map.getType(typeName)).isEqualTo(typeNo);
    }

    @Test
    public void testNoMapping() {

        final NameToTypeNoMap map = new NameToTypeNoMap();

        final TypeName typeName = new TypeName(
                new String [] { "com", "test" },
                null,
                "TestType");

        assertThat(map.getType(typeName)).isNull();
    }
}
