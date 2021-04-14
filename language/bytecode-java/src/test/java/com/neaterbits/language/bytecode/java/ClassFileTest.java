package com.neaterbits.language.bytecode.java;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ClassFileTest {

    @Test
    public void testGetReferenceList() {
        
        final String encodedTypes = 
                "Ljava/util/List;Ljava/util/Map;Ljava/util/Collection;Ljava/util/Set;";
        
        final List<String> types = new ArrayList<>();
        
        ClassFile.getReferenceList(encodedTypes, types::add);
        
        assertThat(types.size()).isEqualTo(4);
        
        assertThat(types.get(0)).isEqualTo("Ljava/util/List;");
        assertThat(types.get(1)).isEqualTo("Ljava/util/Map;");
        assertThat(types.get(2)).isEqualTo("Ljava/util/Collection;");
        assertThat(types.get(3)).isEqualTo("Ljava/util/Set;");
        
    }
}
