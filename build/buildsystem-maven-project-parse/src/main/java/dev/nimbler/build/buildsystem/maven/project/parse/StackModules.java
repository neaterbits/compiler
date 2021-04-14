package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackModules extends StackBase {

	private final List<String> modules;

	StackModules(Context context) {
		super(context);

		this.modules = new ArrayList<>();
	}

	List<String> getModules() {
		return modules;
	}

	void addModule(String module) {
		Objects.requireNonNull(module);

		modules.add(module);
	}
}
