package dev.nimbler.ide.ui.swt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jface.resource.DeviceResourceManager;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import dev.nimbler.ide.model.text.TextModel;
import dev.nimbler.ide.util.ui.RGBColor;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextRange;
import dev.nimbler.ide.util.ui.text.styling.TextStyle;
import dev.nimbler.ide.util.ui.text.styling.TextStyleOffset;
import dev.nimbler.ide.util.ui.text.styling.TextStyles;
import dev.nimbler.ide.util.ui.text.styling.TextStylingModel;

import dev.nimbler.ide.common.ui.view.CursorPositionListener;
import dev.nimbler.ide.common.ui.view.KeyEventListener;
import dev.nimbler.ide.common.ui.view.TextEditorChangeListener;
import dev.nimbler.ide.common.ui.view.TextSelectionListener;

public final class SWTStyledText {

    private final StyledText textWidget;
    
    private final LocalResourceManager resourceManager;
    private final Map<RGBColor, Color> swtColors;
    
    private TextModel textModel;
    
    public SWTStyledText(Composite composite) {
        
        this.textWidget = new StyledText(composite, SWT.NONE);
        
        this.resourceManager = new LocalResourceManager(new DeviceResourceManager(composite.getDisplay()));
        this.swtColors = new HashMap<>();
        
        final Font font = resourceManager.createFont(new SingleFontDescriptor("Monospace", 10, SWT.NONE));
        
        textWidget.addDisposeListener(event -> resourceManager.dispose());
        textWidget.setFont(font);
    }

    public Control getTextWidget() {
        return textWidget;
    }

    public Text getText() {
        return new StringText(textWidget.getText());
    }

    public void setCursorPos(int pos) {
        textWidget.setCaretOffset(pos);
    }

    public int getCursorPos() {
        return textWidget.getCaretOffset();
    }

    public void setFocus() {
        textWidget.setFocus();
    }

    public void setTabs(int tabs) {
        textWidget.setTabs(tabs);
    }

    
    public void addKeyListener(KeyListener keyListener) {
        textWidget.addKeyListener(keyListener);
    }
    
    public TextRange getSelection() {
        final Point selection = textWidget.getSelection();
        
        return selection != null
                ? new TextRange(selection.x,  selection.y - selection.x + 1)
                : null; 
    }

    public void addTextChangeListener(TextEditorChangeListener listener) {
        
        textWidget.addExtendedModifyListener(event -> {
            System.out.println("## text change");
        
            listener.onTextChange(event.start, event.length, event.replacedText != null ? new StringText(event.replacedText) : null);
        });
    }
    
    public void addTextSelectionListener(TextSelectionListener textSelectionListener) {
        textWidget.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                textSelectionListener.onTextSelectionChange(hasSelectedText());
            }
        });
    }

    public void addCursorPositionListener(CursorPositionListener cursorPositionListener) {

        textWidget.addCaretListener(event -> cursorPositionListener.onCursorPositionChanged(event.caretOffset));
        
    }

    public void addKeyListener(KeyEventListener listener) {

        final SWTKeyEventListener keyEventListener = new SWTKeyEventListener(listener);
        
        textWidget.addKeyListener(keyEventListener);
        
        textWidget.addVerifyKeyListener(new VerifyKeyListener() {
            
            @Override
            public void verifyKey(VerifyEvent event) {
                event.doit = keyEventListener.keyPressed(event.character, event.keyCode, event.keyLocation, event.stateMask);
            }
        });
    }

    public void setTextModel(TextModel textModel) {

        Objects.requireNonNull(textModel);

        this.textModel = textModel;

        textWidget.setContent(new StyledTextContent() {
            
            @Override
            public void setText(String text) {
                textModel.setText(new StringText(text));;
            }
            
            @Override
            public void replaceTextRange(int start, int length, String replaceText) {
                textModel.replaceTextRange(start, length, new StringText(replaceText));
            }
            
            @Override
            public void removeTextChangeListener(TextChangeListener arg0) {
                
            }
            
            @Override
            public String getTextRange(int start, int length) {
                return textModel.getTextRange(start, length).asString();
            }
            
            @Override
            public int getOffsetAtLine(int lineIndex) {
                return (int)textModel.getOffsetAtLine(lineIndex);
            }
            
            @Override
            public String getLineDelimiter() {
                return textModel.getLineDelimiter().asString();
            }
            
            @Override
            public int getLineCount() {
                return (int)textModel.getLineCount();
            }
            
            @Override
            public int getLineAtOffset(int offset) {
                return (int)textModel.getLineAtOffset(offset);
            }
            
            @Override
            public String getLine(int lineIndex) {
                return textModel.getLineWithoutAnyNewline(lineIndex).asString();
            }
            
            @Override
            public int getCharCount() {
                return (int)textModel.getCharCount();
            }
            
            @Override
            public void addTextChangeListener(TextChangeListener arg0) {
                
            }
        });
    }
    
    public void setStylingModel(TextStylingModel stylingModel) {
        
        textWidget.addLineStyleListener(event -> {
            
            final Collection<TextStyleOffset> offsets = stylingModel.getLineStyleOffsets(event.lineOffset, event.lineText.length(), new StringText(event.lineText));
            
            event.styles = makeStyleRanges(offsets);
            
        });
    }

    public void cut() {
        textWidget.cut();
    }

    public void copy() {
        textWidget.copy();
    }

    public void paste() {
        textWidget.paste();
    }

    public void selectAll() {
        textWidget.setSelection(0, textWidget.getCharCount());
    }
    
    public void select(long offset, long length) {
        textWidget.setSelection((int)offset, (int)(offset + length));
    }

    public void triggerStylingRefresh() {

        if (textModel != null) {
            int cursorPosition = getCursorPos();
            
            setTextModel(textModel);
            
            setCursorPos(cursorPosition);
        }
    }

    public boolean hasSelectedText() {
        return textWidget.isTextSelected();
    }

    private Color getColor(RGBColor rgb) {
        
        Color color = swtColors.get(rgb);
        
        if (color == null) {
            color = resourceManager.createColor(new RGB(
                    rgb.getR(),
                    rgb.getG(),
                    rgb.getB()));
            
            swtColors.put(rgb, color);
            
        }
        
        return color;
    }

    private StyleRange [] makeStyleRanges(Collection<TextStyleOffset> styles) {

        final StyleRange [] result = new StyleRange[styles.size()];
        
        int dstIdx = 0;
        
        for (TextStyleOffset style : styles) {
            
            result[dstIdx ++] = new StyleRange(
                    (int)style.getStart(),
                    (int)style.getLength(),
                    getColor(style.getColor()),
                    null,
                    getFontStyles(style.getStyles()));
            
        }
        
        return result;
    }
    
    private static int getFontStyles(TextStyles styles) {
        
        int fontStyles = SWT.NONE;
        
        if (styles != null) {
            if (styles.isSet(TextStyle.UNDERLINE)) {
                fontStyles |= SWT.UNDERLINE_SINGLE;
            }
            
            if (styles.isSet(TextStyle.ITALICS)) {
                fontStyles |= SWT.ITALIC;
            }
            
            if (styles.isSet(TextStyle.BOLD)) {
                fontStyles |= SWT.BOLD;
            }
        }
        
        return fontStyles;
    }
}
