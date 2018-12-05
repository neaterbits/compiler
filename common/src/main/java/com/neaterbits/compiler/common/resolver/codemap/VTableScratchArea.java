package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Arrays;

public final class VTableScratchArea {

	private int count;
	private int [] vtableIdx;
	private final IntSet addedMethods;

	public VTableScratchArea() {
		this.vtableIdx = new int[50];
		this.addedMethods = new IntSet(50);
	}
	
	void add(int type, int methodIdx, int vtableIndex) {
		if (count == vtableIdx.length) {
			this.vtableIdx = new int[vtableIdx.length * 3];
		}
		
		vtableIdx[count ++] = vtableIndex;
	}
	
	IntSet getAddedMethods() {
		return addedMethods;
	}

	void clear() {
		this.count = 0;
		addedMethods.clear();
	}

	public int [] copyVTable() {
		return Arrays.copyOf(vtableIdx, count);
	}
}
