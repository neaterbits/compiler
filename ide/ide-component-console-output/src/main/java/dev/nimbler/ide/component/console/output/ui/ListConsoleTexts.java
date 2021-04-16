package dev.nimbler.ide.component.console.output.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

final class ListConsoleTexts extends ConsoleTexts {

    private final List<ConsoleText> texts;

    ListConsoleTexts() {
        this.texts = new ArrayList<>();
    }

    @Override
    ConsoleText getLast() {

        return texts.isEmpty() ? null : texts.get(texts.size() - 1);
    }

    @Override
    ConsoleText getFirst() {

        return texts.isEmpty() ? null : texts.get(0);
    }

    @Override
    ConsoleText getLast(int count) {

        return texts.get(texts.size() - 1 - count);
    }

    @Override
    void replaceLast(ConsoleText other) {

        Objects.requireNonNull(other);
        
        texts.set(texts.size() - 1, other);
    }

    @Override
    List<ConsoleText> removeFirst(int count) {
    	
    	final List<ConsoleText> removed;
        
    	if (count <= 0) {
    		throw new IllegalArgumentException();
    	}
    	
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        else if (count > texts.size()) {
            throw new IllegalArgumentException();
        }
        else if (count == texts.size()) {
        	
        	removed = new ArrayList<>(texts);
        	
            texts.clear();
        }
        else {
            final ConsoleText upTo = texts.get(count);
            
            removed = new ArrayList<>(count);
            
            final Iterator<ConsoleText> iter = texts.iterator();

            int numRemoved = 0;
            while (iter.hasNext()) {
            	final ConsoleText text = iter.next();
            	
            	if (text == upTo) {
            		break;
            	}
            	
            	removed.add(text);
            	iter.remove();
            	
            	++ numRemoved;
            }
            
            if (numRemoved != count) {
            	throw new IllegalStateException();
            }
        }

        return removed;
    }

    @Override
    boolean isEmpty() {
        
        return texts.isEmpty();
    }

    @Override
    int size() {

        return texts.size();
    }

    @Override
    void add(ConsoleText text) {
        
        Objects.requireNonNull(text);
        
        if (isEmpty()) {
            if (text.getOffsetFromStart() != 0L) {
                throw new IllegalArgumentException();
            }
        }
        else {
            final ConsoleText last = getLast();

            if (text.getOffsetFromStart() != last.getOffsetFromStart() + last.getCharCount()) {
                throw new IllegalArgumentException();
            }
        }
     
        texts.add(text);
    }

    List<ConsoleText> getTextsInRange(long startPos, long length) {

        final List<ConsoleText> results;
        
        if (startPos < 0) {
            throw new IllegalArgumentException();
        }
        
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        else if (length == 0L) {
            results = Collections.emptyList();
        }
        else {
            results = new ArrayList<>();
        
            for (ConsoleText text : texts) {
                
                final long charCount = text.getCharCount();
                
                if (
                        (    startPos >= text.getOffsetFromStart()
                          && startPos < text.getOffsetFromStart() + charCount)
                        
                        ||
                        
                        (    startPos < text.getOffsetFromStart()
                          && startPos + length > text.getOffsetFromStart())) {
                            
    
                    results.add(text);
                }
            }
        }

        return results;
    }

	@Override
	List<ConsoleText> getTextsMatching(ConsoleTextMatcher matcher) {

        final List<ConsoleText> results;
		
		if (texts.isEmpty()) {
			results = Collections.emptyList();
		}
		else {
			final Iterator<ConsoleText> iter = texts.iterator();
            
            results = new ArrayList<>();

			while (iter.hasNext()) {

			    final ConsoleText toMatch = iter.next();
			
			    if (matcher.isMatching(toMatch)) {
			        
			        results.add(toMatch);
			        
			        while (iter.hasNext()) {
			            
			            final ConsoleText furtherMatching = iter.next();
			            
			            if (!matcher.isMatching(furtherMatching)) {
			                
			                // Found all matching
			                break;
			            }
			            
			            results.add(furtherMatching);
			        }
			        
			        break;
			    }
			}
		}
		
		return results;
	}

	@Override
	public String toString() {
		return texts.toString();
	}
}
