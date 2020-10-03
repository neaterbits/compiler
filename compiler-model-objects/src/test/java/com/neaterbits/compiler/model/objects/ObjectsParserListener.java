package com.neaterbits.compiler.model.objects;

import java.util.Collection;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.parser.iterative.UntypedIterativeOOParserListener;
import com.neaterbits.compiler.ast.objects.type.primitive.BuiltinType;
import com.neaterbits.compiler.parser.listener.common.ContextAccess;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.util.io.strings.StringSource;

public final class ObjectsParserListener extends UntypedIterativeOOParserListener {

    private static ContextAccess makeContextAccess() {

        final ContextAccess contextAccess = new ContextAccess() {

            @Override
            public Context getContext(int index) {
                return new ImmutableContext(0, 0);
            }

            @Override
            public int writeContext(int otherContext) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int writeContext(Context context) {
                throw new UnsupportedOperationException();
            }
        };

        return contextAccess;
    }

    public ObjectsParserListener(
            StringSource stringSource,
            Collection<BuiltinType> builtinTypes,
            ASTParseTreeFactory.GetBuiltinTypeNo getBuiltinTypeNo) {

        super(stringSource, makeContextAccess(), null, new ASTParseTreeFactory(builtinTypes, getBuiltinTypeNo));
    }
}
