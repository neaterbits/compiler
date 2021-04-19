package dev.nimbler.ide.code.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.jutils.concurrency.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.scheduling.AsyncExecutor;
import org.jutils.concurrency.scheduling.Constraint;
import org.jutils.concurrency.scheduling.ScheduleFunction;
import org.jutils.concurrency.scheduling.ScheduleListener;
import org.jutils.concurrency.scheduling.task.TaskContext;
import org.jutils.structuredlog.binary.logging.LogContext;
import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.ide.code.BaseListeners;
import dev.nimbler.ide.common.codeaccess.TasksAccess;
import dev.nimbler.ide.common.tasks.TaskInfo;
import dev.nimbler.ide.common.tasks.TaskName;
import dev.nimbler.ide.common.tasks.TasksCompleteUpdate;
import dev.nimbler.ide.common.tasks.TasksListener;

public final class TaskManagerImpl extends BaseListeners<TasksListener> implements TaskManager, TasksAccess {

	private final AsyncExecutor asyncExecutor;
	private final ForwardResultToCaller forwardResultToCaller;
	
	private final List<TaskToSchedule> tasksToSchedule;
	
	private final List<RunningTask> runningTasks;
	
	public TaskManagerImpl(AsyncExecutor asyncExecutor, ForwardResultToCaller forwardResultToCaller) {
		
		Objects.requireNonNull(asyncExecutor);
		Objects.requireNonNull(forwardResultToCaller);

		this.asyncExecutor = asyncExecutor;
		this.forwardResultToCaller = forwardResultToCaller;
		
		this.tasksToSchedule = new ArrayList<>();
		
		this.runningTasks = new ArrayList<>();
	}
	
	private boolean hasTask(TaskName taskName) {
		
		Objects.requireNonNull(taskName);
		
		return     tasksToSchedule.stream().anyMatch(t -> t.getName().equals(taskName))
				|| runningTasks.stream().anyMatch(t -> t.getName().equals(taskName));
	}
	
	@Override
	public void forward(Runnable runnable) {

		Objects.requireNonNull(runnable);
		
		forwardResultToCaller.forward(runnable);
	}

	@Override
	public <T, R> TaskName scheduleTask(
			String name,
			String description,
			Constraint constraint,
			T param,
			ScheduleFunction<T, R> function,
			ScheduleListener<T, R> listener,
			TaskName ... runAfter) {

		final TaskName taskName = new TaskName(name, description);
		
		if (hasTask(taskName)) {
			throw new IllegalArgumentException();
		}
		
		final Runnable runnable = () -> asyncExecutor.schedule(
				constraint,
				param,
				function,
				(parameter, result) -> {
					
					if (listener != null) {
						listener.onScheduleResult(parameter, result);
					}
					
					onComplete(taskName);
				});

		addTaskToSchedule(taskName, runnable, false, runAfter);

		scheduleTasks();

		return taskName;
	}

	@Override
	public <CONTEXT extends TaskContext> TaskName scheduleTargets(
			
			String name,
			String description,
			TargetBuilderSpec<CONTEXT> spec,
			String targetName,
			Supplier<CONTEXT> context,
			TaskName ... runAfter) {
		
		final TaskName taskName = new TaskName(name, description);

		if (hasTask(taskName)) {
			throw new IllegalArgumentException();
		}

		final LogContext logContext = new LogContext();

		final Runnable runnable = () -> spec.execute(
		        logContext,
		        context.get(),
                "sourcefolders",
		        new PrintlnTargetExecutorLogger(),
		        asyncExecutor,
		        result -> onComplete(taskName));
		
		addTaskToSchedule(taskName, runnable, true, runAfter);

		scheduleTasks();

		return taskName;
	}
	
	private void addTaskToSchedule(TaskName taskName, Runnable runnable, boolean progressKnown, TaskName ... runAfter) {

		tasksToSchedule.add(new TaskToSchedule(taskName, runnable, progressKnown, runAfter));
		
		forEach(l -> l.onTaskAdded(taskName, progressKnown));
		
	}
	
	private void onComplete(TaskName taskName) {
		
		try {
			final boolean removed = runningTasks.removeIf(t -> t.getName().equals(taskName));
		
			if (!removed) {
				throw new IllegalStateException("Expected removed");
			}
		}
		finally {

			try {
				forEach(l -> l.onTaskComplete(taskName));
			}
			finally {
				scheduleTasks();
			}
		}
	}
	
	private void scheduleTasks() {
		
		final Iterator<TaskToSchedule> iter = tasksToSchedule.iterator();
		
		while (iter.hasNext()) {
			
			final TaskToSchedule task = iter.next();
			
			boolean hasRunAfter = false;
			
			// Are any of the tasks to run after not yet run or complete?  
			for (TaskName runAfter : task.getRunAfter()) {

				if (hasTask(runAfter)) {
					hasRunAfter = true;
					break;
				}
			}

			if (!hasRunAfter) {

				iter.remove();

				final RunningTask runningTask = new RunningTask(task);

				runningTasks.add(runningTask);
				
				task.run();
			}
		}
	}

	@Override
	public void addTasksListener(TasksListener listener) {
		addListener(listener);
	}

	@Override
	public void triggerCompleteUpdate() {

		final List<TaskInfo> update = new ArrayList<>(tasksToSchedule.size() + runningTasks.size());

		for (RunningTask runningTask : runningTasks) {
			
			final TaskInfo taskInfo = new TaskInfo(
					runningTask.getName(),
					true,
					runningTask.isProgressKnown(),
					runningTask.getProgress(),
					runningTask.getNumItems());
			
			update.add(taskInfo);
		}

		for (TaskToSchedule taskToSchedule : tasksToSchedule) {
			
			final TaskInfo taskInfo = new TaskInfo(
					taskToSchedule.getName(),
					false,
					taskToSchedule.isProgressKnown(),
					0,
					0);
		
			update.add(taskInfo);
		}
		
		final TasksCompleteUpdate completeUpdate = new TasksCompleteUpdate(update);

		forEach(l -> l.onCompleteUpdate(completeUpdate));
	}
}
