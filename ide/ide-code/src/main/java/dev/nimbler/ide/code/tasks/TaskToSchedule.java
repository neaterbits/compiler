package dev.nimbler.ide.code.tasks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dev.nimbler.ide.common.tasks.TaskName;

final class TaskToSchedule {

	private final TaskName name;
	
	private final Runnable runnable;
	
	private final boolean progressKnown;
	
	private final List<TaskName> runAfter;
	
	TaskToSchedule(TaskName name, Runnable runnable, boolean progressKnown, TaskName ... runAfter) {
		this.name = name;
		this.runnable = runnable;
		this.progressKnown = progressKnown;
		this.runAfter = Collections.unmodifiableList(Arrays.asList(runAfter));
	}

	TaskName getName() {
		return name;
	}

	boolean isProgressKnown() {
		return progressKnown;
	}

	void run() {
		runnable.run();
	}

	List<TaskName> getRunAfter() {
		return runAfter;
	}

	@Override
	public String toString() {
		return "TaskToSchedule [name=" + name + ", runAfter=" + runAfter + "]";
	}
}
