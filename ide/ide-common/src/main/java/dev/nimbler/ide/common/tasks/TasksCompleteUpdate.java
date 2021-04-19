package dev.nimbler.ide.common.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TasksCompleteUpdate {
	
	private final List<TaskInfo> tasks;

	public TasksCompleteUpdate(List<TaskInfo> tasks) {

		Objects.requireNonNull(tasks);
		
		this.tasks = Collections.unmodifiableList(new ArrayList<>(tasks));
	}

	public List<TaskInfo> getTasks() {
		return tasks;
	}
}
