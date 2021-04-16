package dev.nimbler.ide.component.tasks.ui;

import org.eclipse.swt.widgets.Control;

import dev.nimbler.ide.common.tasks.TaskName;
import dev.nimbler.ide.common.tasks.TasksCompleteUpdate;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.tasks.TasksComponent;
import dev.nimbler.ide.component.common.ui.ComponentCompositeContext;
import dev.nimbler.ide.component.common.ui.DetailsComponentUI;
import dev.nimbler.ide.ui.swt.SWTCompositeUIContext;

public final class TasksUIComponent implements DetailsComponentUI<Control>, TasksComponent {

	private SWTTasksView tasksView;

	@Override
	public Control addCompositeComponentUI(ComponentCompositeContext context, ComponentIDEAccess componentIDEAccess) {

		final SWTCompositeUIContext swtContext = (SWTCompositeUIContext)context;
		
		this.tasksView = new SWTTasksView(swtContext.getComposite());
		
		return tasksView.getControl();
	}

	@Override
	public void onTaskAdded(TaskName task, boolean progressKnown) {
		tasksView.onTaskAdded(task, progressKnown);
	}

	@Override
	public void onTaskProgress(TaskName task, int progress, int numItems) {
		tasksView.onTaskProgress(task, progress, numItems);
	}

	@Override
	public void onTaskComplete(TaskName task) {
		tasksView.onTaskComplete(task);
	}

	@Override
	public void onCompleteUpdate(TasksCompleteUpdate update) {
		tasksView.onCompleteUpdate(update);
	}
}
