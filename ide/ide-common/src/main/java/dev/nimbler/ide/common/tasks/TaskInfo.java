package dev.nimbler.ide.common.tasks;

import java.util.Objects;

public final class TaskInfo {

	private final TaskName name;
	
	private final boolean running;
	
	private final boolean progressKnown;
	
	private final int processedItems;
	private final int numItems;

	public TaskInfo(TaskName name, boolean running, boolean progressKnown, int processedItems, int numItems) {

		Objects.requireNonNull(name);
		
		this.name = name;
		this.running = running;

		this.progressKnown = progressKnown;
		this.processedItems = processedItems;
		this.numItems = numItems;
	}
	
	public TaskName getName() {
		return name;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isProgressKnown() {
		return progressKnown;
	}

	public int getProcessedItems() {
		return processedItems;
	}

	public int getNumItems() {
		return numItems;
	}
}
