package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.StackEntry;

public abstract class BaseStackTryCatchFinally<STATEMENT, CATCH_BLOCK> extends StackEntry {

	private StackTryBlock<STATEMENT> tryBlock;
	private final List<CATCH_BLOCK> catchBlocks;
	private StackFinallyBlock<STATEMENT> finallyBlock;
	
	protected BaseStackTryCatchFinally(ParseLogger parseLogger) {
		super(parseLogger);

		this.catchBlocks = new ArrayList<>();
	}

	public final StackTryBlock<STATEMENT> getTryBlock() {
		return tryBlock;
	}

	public final void setTryBlock(StackTryBlock<STATEMENT> tryBlock) {
		this.tryBlock = tryBlock;
	}

	public final void addCatchBlock(CATCH_BLOCK catchBlock) {
		Objects.requireNonNull(catchBlock);

		catchBlocks.add(catchBlock);
	}
	
	public final List<CATCH_BLOCK> getCatchBlocks() {
		return catchBlocks;
	}

	public final StackFinallyBlock<STATEMENT> getFinallyBlock() {
		return finallyBlock;
	}

	public final void setFinallyBlock(StackFinallyBlock<STATEMENT> finallyBlock) {
		this.finallyBlock = finallyBlock;
	}
}
