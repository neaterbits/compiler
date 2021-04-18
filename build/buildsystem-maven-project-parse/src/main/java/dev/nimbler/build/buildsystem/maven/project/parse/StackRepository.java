package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenReleases;
import dev.nimbler.build.buildsystem.maven.project.model.MavenSnapshots;

final class StackRepository
        extends StackBase
        implements NameSetter, UrlSetter, IdSetter, LayoutSetter {

    private MavenReleases releases;
    
    private MavenSnapshots snapshots;
    
    private String name;
    
    private String id;
    
    private String url;
    
    private String layout;

    StackRepository(Context context) {
        super(context);
    }

    MavenReleases getReleases() {
        return releases;
    }

    void setReleases(MavenReleases releases) {
        this.releases = releases;
    }

    MavenSnapshots getSnapshots() {
        return snapshots;
    }

    void setSnapshots(MavenSnapshots snapshots) {
        this.snapshots = snapshots;
    }

    String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    String getLayout() {
        return layout;
    }

    @Override
    public void setLayout(String layout) {
        this.layout = layout;
    }
}
