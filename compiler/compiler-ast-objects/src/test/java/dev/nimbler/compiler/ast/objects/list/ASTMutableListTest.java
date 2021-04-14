package dev.nimbler.compiler.ast.objects.list;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class ASTMutableListTest {

    private static class Node extends ASTNode {
        
    }
    
    @Test
    public void testGetAtIndex() {
    
        final Node node1 = new Node();
        final Node node2 = new Node();
        
        final ASTMutableList<Node> list = new ASTMutableList<>();
        
        list.add(node1);
        list.add(node2);
        
        final ASTList<Node> l = list;
        
        assertThat(l.get(0) == node1).isTrue();
        assertThat(l.get(1) == node2).isTrue();
    }

    @Test
    public void testGetAtIndexFromCollection() {
    
        final Node node1 = new Node();
        final Node node2 = new Node();
        
        final ASTMutableList<Node> list = new ASTMutableList<>(Arrays.asList(node1, node2));
        
        final ASTList<Node> l = list;
        
        assertThat(l.get(0) == node1).isTrue();
        assertThat(l.get(1) == node2).isTrue();
    }
}
