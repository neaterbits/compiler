package dev.nimbler.ide.common.tasks;

public interface TasksListener {
	
	void onTaskAdded(TaskName task, boolean progressKnown);
	
	void onTaskProgress(TaskName task, int progress, int numItems);
	
	void onTaskComplete(TaskName task);
	
	void onCompleteUpdate(TasksCompleteUpdate update);
	
}
