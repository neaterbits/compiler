package dev.nimbler.compiler.emit.base;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTNode;
import dev.nimbler.compiler.emit.EmitterState;

public class EmitterUtil<T extends EmitterState> {

	protected final <V extends ASTNode> void emitList(T state, ASTList<V> list, String separator, Function<V, String> convert) {

		emitListTo(state, list, separator, e -> state.append(convert.apply(e)));
	}

	protected final <V extends ASTNode> void emitListTo(T state, ASTList<V> list, String separator, Consumer<V> convert) {
		
		list.foreachWithIndex((node, i) ->  {
			if (i > 0 && separator != null) {
				state.append(separator);
			}

			convert.accept(node);
		});
	}

	protected final <V> void emitListTo(T state, List<V> list, String separator, Consumer<V> convert) {
		
		for (int i = 0; i < list.size(); ++ i) {
		
			if (i > 0) {
				state.append(separator);
			}

			convert.accept(list.get(i));
		}
	}
}
