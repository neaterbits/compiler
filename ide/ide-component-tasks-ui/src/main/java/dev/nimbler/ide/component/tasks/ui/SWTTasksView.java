package dev.nimbler.ide.component.tasks.ui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import dev.nimbler.ide.common.tasks.TaskInfo;
import dev.nimbler.ide.common.tasks.TaskName;
import dev.nimbler.ide.common.tasks.TasksCompleteUpdate;
import dev.nimbler.ide.common.tasks.TasksListener;

final class SWTTasksView implements TasksListener {

	private final Composite taskListComposite;
	
	private final HashMap<TaskName, SWTTaskProgressComposite> tasks;
	
	SWTTasksView(Composite composite) {
		
		this.tasks = new HashMap<>();
		
		this.taskListComposite = new Composite(composite, SWT.NONE);
		
		taskListComposite.setLayout(new GridLayout(1, false));
	}
	
	Control getControl() {
		return taskListComposite;
	}

	@Override
	public void onTaskAdded(TaskName task, boolean progressKnown) {
		
		final SWTTaskProgressComposite progressComposite
					= new SWTTaskProgressComposite(taskListComposite, SWT.BORDER, progressKnown);
		
		progressComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		progressComposite.update(task.getDescription());
		
		tasks.put(task, progressComposite);
		
		taskListComposite.layout(true);
	}

	@Override
	public void onTaskProgress(TaskName task, int progress, int numItems) {

		final SWTTaskProgressComposite progressComposite = tasks.get(task);

		progressComposite.update(progress, numItems);
	}

	@Override
	public void onTaskComplete(TaskName task) {

		final SWTTaskProgressComposite progressComposite = tasks.remove(task);
		
		progressComposite.dispose();

		taskListComposite.layout(true);
	}

	@Override
	public void onCompleteUpdate(TasksCompleteUpdate update) {

		tasks.values().forEach(Control::dispose);
		
		tasks.clear();
		
		for (TaskInfo t : update.getTasks()) {
			onTaskAdded(t.getName(), t.isProgressKnown());
			
			if (t.isRunning()) {
				onTaskProgress(t.getName(), t.getProcessedItems(), t.getNumItems());
			}
		}
	}
}
