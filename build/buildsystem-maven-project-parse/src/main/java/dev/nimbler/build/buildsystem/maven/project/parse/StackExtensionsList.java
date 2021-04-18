package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenExtension;

final class StackExtensionsList extends StackBase {

	private final List<MavenExtension> extensions;

	StackExtensionsList(Context context) {
		super(context);

		this.extensions = new ArrayList<>();
	}

	public List<MavenExtension> getExtensions() {
		return extensions;
	}

	public void addExtension(MavenExtension extension) {

		Objects.requireNonNull(extension);

		extensions.add(extension);
	}
}
