package dev.nimbler.ide.component.console.output.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import dev.nimbler.ide.component.common.output.UIProcessOutputComponent.ProcessSource;
import dev.nimbler.ide.util.ui.text.LineDelimiter;

public class ConsoleMain {

    public static void main(String [] args) {
    	
    	final Display display = Display.getDefault();

		display.setErrorHandler(error -> {
			
			System.out.println("## exception " + error);
		});
		
		display.setRuntimeExceptionHandler(ex -> {
			
			System.out.println("## runtimeexception " + ex);
			
			ex.printStackTrace();
		});

    	final Shell window = new Shell(display, SWT.SHELL_TRIM);
    	
    	window.setText("Console test");
    	
    	final LineDelimiter lineDelimiter = LineDelimiter.getSystemLineDelimiter();
    	
    	window.setLayout(new FillLayout());
    	
    	final SWTConsoleOutputView output = new SWTConsoleOutputView(
    			window,
    			SWT.NONE,
    			lineDelimiter,
    			50);

		for (int i = 0; i < 10; ++ i) {
			
			final String text = "Line " + (i + 1) + lineDelimiter.asString();
			
			try {
				output.addData(ProcessSource.STDOUT, text);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}

    	output.setLayout(new FillLayout());

    	window.open();

    	while (!window.isDisposed()) {

    		if (!display.readAndDispatch()) {
    			display.sleep();
    		}
    	}
    }
}
