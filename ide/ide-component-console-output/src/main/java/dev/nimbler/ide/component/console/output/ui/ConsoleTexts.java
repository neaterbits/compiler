package dev.nimbler.ide.component.console.output.ui;

import java.util.List;

abstract class ConsoleTexts {

    abstract ConsoleText getLast();

    abstract ConsoleText getLast(int count);

    abstract void replaceLast(ConsoleText other);

    abstract ConsoleText getFirst();
    
    abstract List<ConsoleText> removeFirst(int count);
    
    abstract boolean isEmpty();

    abstract int size();

    abstract void add(ConsoleText text);
    
    abstract List<ConsoleText> getTextsInRange(long startPos, long length);
    
    @FunctionalInterface
    interface ConsoleTextMatcher{
    	
    	// Match node, once has found initial node
    	boolean isMatching(ConsoleText consoleText);
    }
    
    abstract List<ConsoleText> getTextsMatching(ConsoleTextMatcher matcher);
    
}
