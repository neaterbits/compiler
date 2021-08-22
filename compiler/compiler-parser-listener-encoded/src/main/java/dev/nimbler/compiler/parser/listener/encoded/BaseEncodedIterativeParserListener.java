package dev.nimbler.compiler.parser.listener.encoded;

import org.jutils.io.strings.Tokenizer;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.encoded.AST;
import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;

public abstract class BaseEncodedIterativeParserListener<COMPILATION_UNIT>
    extends BaseInfixParserListener<COMPILATION_UNIT>
    implements IterativeParseTreeListener<COMPILATION_UNIT> {

    protected BaseEncodedIterativeParserListener(String file, Tokenizer tokenizer) {
        super(file, tokenizer);
    }

    @Override
    public final void onIfStatementStart(int ifStatementStartContext, long ifKeyword, int ifKeywordContext) {

        // whole if - else if - else statement
        writeStartElementContextRef(ifStatementStartContext);
        AST.encodeIfElseIfElseStatementStart(astBuffer, ifKeyword, ifKeywordContext);
    }

    @Override
    public void onIfStatementInitialBlockStart(int ifStatementInitialBlockStartContext) {

        writeStartElementContextRef(ifStatementInitialBlockStartContext);
        AST.encodeIfConditionBlockStart(astBuffer);
    }

    @Override
    public final void onIfStatementInitialBlockEnd(int ifStatementInitialBlockStartContext, Context endContext) {

        writeEndElementContext(ifStatementInitialBlockStartContext, endContext);
        
        AST.encodeIfConditionBlockEnd(astBuffer);
    }

    @Override
    public final void onElseIfStatementStart(
            int elseIfStatementStartContext,
            long elseKeyword, int elseKeywordContext,
            long ifKeyword, int ifKeywordContext) {
        
        verifyNotSameContext(elseIfStatementStartContext, elseKeywordContext, ifKeywordContext);
        
        writeStartElementContextRef(elseIfStatementStartContext);
        
        AST.encodeElseIfConditionBlockStart(
                astBuffer,
                elseKeyword, elseKeywordContext,
                ifKeyword, ifKeywordContext);
    }

    @Override
    public final void onElseIfStatementEnd(int elseIfStatementStartContext, Context endContext) {
        
        writeEndElementContext(elseIfStatementStartContext, endContext);
        
        AST.encodeElseIfConditionBlockEnd(astBuffer);
    }
    
    @Override
    public final void onElseStatementStart(int elseStatementStartContext, long elseKeyword, int elseKeywordContext) {
        
        verifyNotSameContext(elseStatementStartContext, elseKeywordContext);

        writeStartElementContextRef(elseStatementStartContext);
        
        AST.encodeElseBlockStart(astBuffer, elseKeyword, elseKeywordContext);
    }

    @Override
    public final void onElseStatementEnd(int elseStatementStartContext, Context endContext) {

        writeEndElementContext(elseStatementStartContext, endContext);
        
        AST.encodeElseBlockEnd(astBuffer);
    }

    @Override
    public final void onEndIfStatement(int ifStatementStartContext, Context endContext) {

        writeEndElementContext(ifStatementStartContext, endContext);
        
        AST.encodeIfElseIfElseStatementEnd(astBuffer);
    }

    @Override
    public final void onSwitchStatementStart(int switchStatementStartContext, long keyword, int keywordContext) {
        
        verifyNotSameContext(switchStatementStartContext, keywordContext);
        
        writeStartElementContextRef(switchStatementStartContext);

        AST.encodeSwitchStatementStart(astBuffer, keyword, keywordContext);
    }

    @Override
    public final void onJavaSwitchBlockStart(int javaSwitchBlockStartContext) {
        
    }

    @Override
    public final void onJavaSwitchBlockStatementGroupStart(int javaSwitchBlockStatementGroupStartContext) {

        writeStartElementContextRef(javaSwitchBlockStatementGroupStartContext);

        AST.encodeSwitchCaseGroupStart(astBuffer);
    }

    @Override
    public final void onSwitchLabelsStart(int switchLabelsStartContext) {

    }

    @Override
    public final void onSwitchLabelsEnd(int switchLabelsStartContext, Context endContext) {

    }

    @Override
    public final void onJavaSwitchBlockStatementGroupEnd(int javaSwitchBlockStatementGroupStartContext, Context endContext) {

        writeEndElementContext(javaSwitchBlockStatementGroupStartContext, endContext);
        
        AST.encodeSwitchCaseGroupEnd(astBuffer);
    }

    @Override
    public final void onConstantSwitchLabelStart(int constantSwitchLabelStartContext, long keyword, int keywordContext) {
        
        verifyNotSameContext(constantSwitchLabelStartContext, keywordContext);

        writeStartElementContextRef(constantSwitchLabelStartContext);
        
        AST.encodeConstantSwitchLabelStart(astBuffer, keyword, keywordContext);
    }

    @Override
    public final void onConstantSwitchLabelEnd(int constantSwitchLabelStartContext, Context endContext) {
        
        writeEndElementContext(constantSwitchLabelStartContext, endContext);

        AST.encodeConstantSwitchLabelEnd(astBuffer);
    }

    @Override
    public final void onEnumSwitchLabel(int enumSwitchLabelStartContext, long keyword, int keywordContext, long constantName,
            int constantNameContext) {
        
        verifyNotSameContext(enumSwitchLabelStartContext, keywordContext, constantNameContext);
        
        writeStartElementContextRef(enumSwitchLabelStartContext);

        AST.encodeEnumSwitchLabel(astBuffer, keyword, keywordContext, constantName, constantNameContext);
    }

    @Override
    public final void onDefaultSwitchLabel(int leafContext, long keyword) {

        writeLeafElementContextRef(leafContext);

        AST.encodeDefaultSwitchLabel(astBuffer, keyword);
    }

    @Override
    public final void onJavaSwitchBlockEnd(int javaSwitchBlockStartContext, Context endContext) {

    }

    @Override
    public final void onSwitchStatementEnd(int switchStatementStartContext, Context endContext) {

        writeEndElementContext(switchStatementStartContext, endContext);
        
        AST.encodeSwitchStatementEnd(astBuffer);
    }

    @Override
    public final void onBreakStatement(
            int breakStatementStartContext,
            long keyword,
            int keywordContext,
            long label,
            int labelContext,
            Context endContext) {

        writeStartElementContextRef(breakStatementStartContext);

        AST.encodeBreakStatement(astBuffer, keyword, keywordContext, label, labelContext);

        writeEndElementContext(breakStatementStartContext, endContext);
    }
}
