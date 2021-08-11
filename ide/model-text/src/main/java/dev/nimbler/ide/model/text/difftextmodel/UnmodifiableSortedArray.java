package dev.nimbler.ide.model.text.difftextmodel;

interface UnmodifiableSortedArray<T> {

    boolean isEmpty();
    
    int length();
    
    T get(int index);
}
