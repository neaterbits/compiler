package dev.nimbler.ide.ui.actions.contexts.source;

import java.util.Objects;

import dev.nimbler.compiler.model.common.ISourceToken;
import dev.nimbler.compiler.model.common.SourceTokenType;
import dev.nimbler.ide.common.model.source.ISourceTokenProperties;

public class SourceTokenContext extends SourceContext {

	private final ISourceToken token;
	private final ISourceTokenProperties tokenProperties;

	public SourceTokenContext(ISourceToken token, ISourceTokenProperties tokenProperties) {

		Objects.requireNonNull(token);
		Objects.requireNonNull(tokenProperties);
		
		this.token = token;
		this.tokenProperties = tokenProperties;
	}

	public SourceTokenType getTokenType() {
		return token.getTokenType();
	}

	public ISourceTokenProperties getTokenProperties() {
		return tokenProperties;
	}
}
