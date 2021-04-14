package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenBuildPlugin;

final class StackPlugins extends StackBase {

	private final List<MavenBuildPlugin> plugins;

	StackPlugins(Context context) {
		super(context);

		this.plugins = new ArrayList<>();
	}

	List<MavenBuildPlugin> getPlugins() {
		return plugins;
	}

	void addPlugin(MavenBuildPlugin plugin) {

		Objects.requireNonNull(plugin);

		plugins.add(plugin);
	}
}
