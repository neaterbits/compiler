package dev.nimbler.ide.common.tasks;

import java.util.Objects;

public class TaskName {

	private final String id;
	private final String description;

	public TaskName(String id, String description) {
		
		Objects.requireNonNull(id);
		Objects.requireNonNull(description);
		
		this.id = id;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskName other = (TaskName) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskName [id=" + id + ", description=" + description + "]";
	}
}
