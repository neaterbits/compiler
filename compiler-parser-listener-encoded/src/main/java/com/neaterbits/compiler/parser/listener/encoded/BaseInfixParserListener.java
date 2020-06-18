package com.neaterbits.compiler.parser.listener.encoded;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Notation;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.parser.listener.common.InfixParserListener;
import com.neaterbits.util.io.strings.Tokenizer;

abstract class BaseInfixParserListener<COMPILATION_UNIT>
    extends BaseParserListener<COMPILATION_UNIT> implements InfixParserListener<COMPILATION_UNIT> {

    BaseInfixParserListener(String file, Tokenizer tokenizer) {
        super(file, tokenizer);
    }

    @Override
    public final void onExpressionBinaryOperator(int leafContext, Operator operator) {

        writeLeafElementContextRef(leafContext);
        
        AST.encodeExpressionBinaryOperator(astBuffer, operator);
    }

    @Override
    public final void onIncrementDecrementExpressionStart(
            int incrementDecrementExpressionStartContext,
            Arithmetic operator,
            Notation notation) {
        
        writeStartElementContextRef(incrementDecrementExpressionStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIncrementDecrementExpressionEnd(int incrementDecrementExpressionStartContext, Context endContext) {

        writeEndElementContext(incrementDecrementExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }
}
