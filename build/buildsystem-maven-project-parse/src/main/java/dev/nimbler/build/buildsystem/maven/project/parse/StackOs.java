package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.common.parse.VersionSetter;

final class StackOs
        extends StackBase
        implements NameSetter, VersionSetter {

    private String name;
    private String family;
    private String arch;
    private String version;

    StackOs(Context context) {
        super(context);
    }

    String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    String getFamily() {
        return family;
    }

    void setFamily(String family) {
        this.family = family;
    }

    String getArch() {
        return arch;
    }

    void setArch(String arch) {
        this.arch = arch;
    }

    String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }
}
