package com.neaterbits.compiler.util.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.ModuleId;

public abstract class ModuleSpec {

	private final ModuleId moduleId;
	private final List<ModuleSpec> dependencies;

	public abstract File getBaseDirectory();
	
	public ModuleSpec(ModuleId moduleId, List<ModuleSpec> dependencies) {
		Objects.requireNonNull(moduleId);
		
		this.moduleId = moduleId;
		this.dependencies = dependencies != null ? Collections.unmodifiableList(new ArrayList<>(dependencies)) : Collections.emptyList();
	}

	
	public final List<ModuleSpec> getDependencies() {
		return dependencies;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
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
		ModuleSpec other = (ModuleSpec) obj;
		if (moduleId == null) {
			if (other.moduleId != null)
				return false;
		} else if (!moduleId.equals(other.moduleId))
			return false;
		return true;
	}
}
