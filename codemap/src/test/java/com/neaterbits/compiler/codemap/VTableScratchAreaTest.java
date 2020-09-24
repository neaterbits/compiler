package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class VTableScratchAreaTest {

    @Test
    public void testAdd() {

        final VTableScratchArea scratchArea = new VTableScratchArea();

        final int typeNo = 1;

        scratchArea.add(typeNo, 1000, 0, 1);
        scratchArea.add(typeNo, 1001, 1, 3);
        scratchArea.add(typeNo, 1002, 2, 4);

        int [] vtable = scratchArea.copyVTable();

        assertThat(vtable).isNotNull();
        assertThat(vtable.length).isEqualTo(3);
        assertThat(vtable[0]).isEqualTo(1);
        assertThat(vtable[1]).isEqualTo(3);
        assertThat(vtable[2]).isEqualTo(4);

        assertThat(scratchArea.getNumAddedMethods()).isEqualTo(3);
        assertThat(scratchArea.hasAddedMethod(1000)).isTrue();
        assertThat(scratchArea.hasAddedMethod(1001)).isTrue();
        assertThat(scratchArea.hasAddedMethod(1002)).isTrue();

        scratchArea.clear();

        vtable = scratchArea.copyVTable();
        assertThat(vtable).isNotNull();
        assertThat(vtable.length).isEqualTo(0);

        assertThat(scratchArea.getNumAddedMethods()).isEqualTo(0);
        assertThat(scratchArea.hasAddedMethod(1000)).isFalse();
        assertThat(scratchArea.hasAddedMethod(1001)).isFalse();
        assertThat(scratchArea.hasAddedMethod(1002)).isFalse();
    }
}
