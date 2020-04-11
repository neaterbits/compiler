package com.neaterbits.compiler.parser.listener.encoded;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Notation;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.parser.listener.common.InfixParserListener;
import com.neaterbits.util.io.strings.Tokenizer;

abstract class BaseInfixParserListener<COMPILATION_UNIT>
    extends BaseParserListener<COMPILATION_UNIT> implements InfixParserListener<COMPILATION_UNIT> {

    BaseInfixParserListener(Tokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    public final void onExpressionBinaryOperator(Context context, Operator operator) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIncrementDecrementExpressionStart(Context context, Arithmetic operator, Notation notation) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIncrementDecrementExpressionEnd(Context context) {

        throw new UnsupportedOperationException();
    }
}
