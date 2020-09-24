package com.neaterbits.compiler.codemap;

import java.util.Arrays;

import com.neaterbits.compiler.util.IntSet;

public final class VTableScratchArea {

	private int count;
	private int [] vtableIdx;
	private final IntSet addedMethods;

	public VTableScratchArea() {
		this.vtableIdx = new int[50];
		this.addedMethods = new IntSet(50);
	}

	void add(int type, int methodNo, int methodIdx, int vtableIndex) {
		if (count == vtableIdx.length) {
			this.vtableIdx = new int[vtableIdx.length * 3];
		}

		vtableIdx[count ++] = vtableIndex;

		addedMethods.add(methodNo);
	}

	void clear() {
		this.count = 0;
		addedMethods.clear();
	}

	int getNumAddedMethods() {
	    return addedMethods.size();
	}

	boolean hasAddedMethod(int methodNo) {
	    return addedMethods.contains(methodNo);
	}

	public int [] copyVTable() {
		return Arrays.copyOf(vtableIdx, count);
	}
}
