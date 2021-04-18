package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class StackImport<IDENTIFIER> extends StackEntry {

	private final String importKeyword;
	private final Context importKeywordContext;
	
	private final String staticKeyword;
	private final Context staticKeywordContext;
	
	private final List<IDENTIFIER> identifiers;
	
	public StackImport(ParseLogger logger,
			String importKeyword,
			Context importKeywordContext,
			
			String staticKeyword,
			Context staticKeywordContext
			) {
		
		super(logger);
		
		Objects.requireNonNull(importKeyword);
		Objects.requireNonNull(importKeywordContext);
		
		this.importKeyword = importKeyword;
		this.importKeywordContext = importKeywordContext;
		
		this.staticKeyword = staticKeyword;
		this.staticKeywordContext = staticKeywordContext;
		
		this.identifiers = new ArrayList<>();
	}
	
	public String getImportKeyword() {
		return importKeyword;
	}

	public Context getImportKeywordContext() {
		return importKeywordContext;
	}

	public String getStaticKeyword() {
		return staticKeyword;
	}

	public Context getStaticKeywordContext() {
		return staticKeywordContext;
	}

	public void addIdentifier(IDENTIFIER identifier) {
		
		Objects.requireNonNull(identifier);
		
		identifiers.add(identifier);
	}
	
	public List<IDENTIFIER> getIdentifiers() {
		return identifiers;
	}
}
