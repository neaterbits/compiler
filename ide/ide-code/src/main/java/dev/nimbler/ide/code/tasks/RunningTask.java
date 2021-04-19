package dev.nimbler.ide.code.tasks;

import java.util.Objects;

import dev.nimbler.ide.common.tasks.TaskName;

final class RunningTask {

	private final TaskName name;
	
	private final boolean progressKnown;
	
	private int progress;
	private int numItems;
	
	RunningTask(TaskToSchedule taskToSchedule) {
		this(taskToSchedule.getName(), taskToSchedule.isProgressKnown());
	}

	private RunningTask(TaskName name, boolean progressKnown) {

		Objects.requireNonNull(name);
		
		this.name = name;
		this.progressKnown = progressKnown;
	}

	TaskName getName() {
		return name;
	}

	void updateProgress(int progress, int numItems) {
		this.progress = progress;
		this.numItems = numItems;
	}

	boolean isProgressKnown() {
		return progressKnown;
	}

	int getProgress() {
		return progress;
	}

	int getNumItems() {
		return numItems;
	}

	@Override
	public String toString() {
		return "RunningTask [name=" + name + ", progress=" + progress + ", numItems=" + numItems + "]";
	}
}
