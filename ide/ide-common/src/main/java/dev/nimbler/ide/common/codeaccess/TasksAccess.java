package dev.nimbler.ide.common.codeaccess;

import dev.nimbler.ide.common.tasks.TasksListener;

public interface TasksAccess {

	void addTasksListener(TasksListener listener);
	
	void triggerCompleteUpdate();
}
