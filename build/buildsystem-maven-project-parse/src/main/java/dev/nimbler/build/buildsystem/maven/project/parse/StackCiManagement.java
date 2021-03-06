package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenNotifier;

final class StackCiManagement
        extends StackBase
        implements SystemSetter, UrlSetter {

    private String system;

    private String url;

    private List<MavenNotifier> notifiers;

    StackCiManagement(Context context) {
        super(context);
    }

    String getSystem() {
        return system;
    }

    @Override
    public void setSystem(String system) {
        this.system = system;
    }

    String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    List<MavenNotifier> getNotifiers() {
        return notifiers;
    }

    void setNotifiers(List<MavenNotifier> notifiers) {
        this.notifiers = notifiers;
    }
}
