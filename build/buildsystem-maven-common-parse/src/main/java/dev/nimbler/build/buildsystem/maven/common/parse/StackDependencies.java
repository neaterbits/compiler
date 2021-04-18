package dev.nimbler.build.buildsystem.maven.common.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;

public final class StackDependencies extends StackBase {

	private final List<MavenDependency> dependencies;

	public StackDependencies(Context context) {
		super(context);

		this.dependencies = new ArrayList<>();
	}

	public List<MavenDependency> getDependencies() {
		return dependencies;
	}

	public void addDependency(MavenDependency dependency) {
		Objects.requireNonNull(dependency);

		dependencies.add(dependency);
	}
}
