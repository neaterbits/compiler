package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackIssueManagement
        extends StackBase
        implements SystemSetter, UrlSetter {

    private String system;
    private String url;

    StackIssueManagement(Context context) {
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
}
