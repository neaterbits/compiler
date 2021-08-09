package dev.nimbler.ide.changehistory;

import java.util.List;

abstract class BaseChangeStore {

    static void iterate(
            List<? extends HistoryChangeRef> changeRefs,
            boolean chronological,
            ChangeStoreIterator iterator) {

        final int length = changeRefs.size();

        boolean done = false;
        
        if (chronological) {
            for (int i = 0; i < length && !done; ++ i) {
                done = callIterator(iterator, changeRefs.get(i));
            }
        }
        else {
            for (int i = length - 1; i >= 0 && !done; -- i) {
                done = callIterator(iterator, changeRefs.get(i));
            }
        }
    }
    
    private static boolean callIterator(ChangeStoreIterator iterator, HistoryChangeRef ref) {
        
        final boolean done;
        
        switch (iterator.onChange(ref, ref.getHistoryChangeRef())) {
        case CONTINUE:
            done = false;
            break;

        case EXIT:
            done = true;
            break;

        default:
            throw new IllegalStateException();
        }
        
        return done;
    }
}
