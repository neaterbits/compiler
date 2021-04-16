package dev.nimbler.ide.component.console.output.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import dev.nimbler.ide.component.common.output.UIProcessOutputComponent.ProcessSource;
import dev.nimbler.ide.ui.swt.SWTStyledText;
import dev.nimbler.ide.util.ui.text.LineDelimiter;

final class SWTConsoleOutputView extends Composite {

    private final SWTStyledText styled;

    private final ConsoleTextModel textModel;
    
    public SWTConsoleOutputView(Composite composite, int style, LineDelimiter lineDelimiter, int maxDisplayed) {
        super(composite, style);
    
        this.styled = new SWTStyledText(this, SWT.READ_ONLY);
        
        this.textModel = new ConsoleTextModel(lineDelimiter, maxDisplayed);
        
        styled.setTextModel(textModel);
    }
    
    void addData(ProcessSource source, String data) {

        textModel.add(source, new String(data));
    }
}
