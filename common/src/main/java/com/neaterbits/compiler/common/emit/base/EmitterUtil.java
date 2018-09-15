package com.neaterbits.compiler.common.emit.base;

import java.util.List;
import java.util.function.Function;

import com.neaterbits.compiler.common.emit.EmitterState;

public class EmitterUtil<T extends EmitterState> {

	protected final <V> void emitList(T state, List<V> list, String separator, Function<V, String> convert) {
		
		for (int i = 0; i < list.size(); ++ i) {
			if (i > 0) {
				state.append(separator);
			}
			
			state.append(convert.apply(list.get(i)));
		}
	}
}
