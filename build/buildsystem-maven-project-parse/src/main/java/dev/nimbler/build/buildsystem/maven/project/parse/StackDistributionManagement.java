package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenDistributionManagementRelocation;
import dev.nimbler.build.buildsystem.maven.project.model.MavenDistributionManagementRepository;
import dev.nimbler.build.buildsystem.maven.project.model.MavenDistributionManagementSite;

final class StackDistributionManagement extends StackBase {

    private String downloadUrl;
    private String status;
    private MavenDistributionManagementRepository repository;
    private MavenDistributionManagementRepository snapshotRepository;
    private MavenDistributionManagementSite site;
    private MavenDistributionManagementRelocation relocation;
    
    StackDistributionManagement(Context context) {
        super(context);
    }

    String getDownloadUrl() {
        return downloadUrl;
    }

    void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    MavenDistributionManagementRepository getRepository() {
        return repository;
    }

    void setRepository(MavenDistributionManagementRepository repository) {
        this.repository = repository;
    }

    MavenDistributionManagementRepository getSnapshotRepository() {
        return snapshotRepository;
    }

    void setSnapshotRepository(MavenDistributionManagementRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    MavenDistributionManagementSite getSite() {
        return site;
    }

    void setSite(MavenDistributionManagementSite site) {
        this.site = site;
    }

    MavenDistributionManagementRelocation getRelocation() {
        return relocation;
    }

    void setRelocation(MavenDistributionManagementRelocation relocation) {
        this.relocation = relocation;
    }
}
