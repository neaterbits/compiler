package com.neaterbits.compiler.common.emit.base;

import java.util.function.Consumer;
import java.util.function.Function;

import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTNode;
import com.neaterbits.compiler.common.emit.EmitterState;

public class EmitterUtil<T extends EmitterState> {

	protected final <V extends ASTNode> void emitList(T state, ASTList<V> list, String separator, Function<V, String> convert) {

		emitListTo(state, list, separator, e -> state.append(convert.apply(e)));
	}

	protected final <V extends ASTNode> void emitListTo(T state, ASTList<V> list, String separator, Consumer<V> convert) {
		
		list.foreachWithIndex((node, i) ->  {
			if (i > 0) {
				state.append(separator);
			}

			convert.accept(node);
		});
	}

}
