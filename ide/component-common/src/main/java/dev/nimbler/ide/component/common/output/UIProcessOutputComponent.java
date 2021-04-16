package dev.nimbler.ide.component.common.output;

public interface UIProcessOutputComponent extends UIStreamOutputComponent {

    public enum ProcessSource {
        STDOUT,
        STDERR
    }
    
    void output(ProcessSource source, String data);

}
