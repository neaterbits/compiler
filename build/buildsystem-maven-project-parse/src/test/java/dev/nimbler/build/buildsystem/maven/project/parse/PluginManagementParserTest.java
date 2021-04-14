package dev.nimbler.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import dev.nimbler.build.buildsystem.maven.project.model.MavenBuild;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderException;

public class PluginManagementParserTest extends BasePomParserTest {

    @Test
    public void testPluginManagement() throws IOException, XMLReaderException {
    
        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";
    
        final String pomString =
                "<project>"
    
              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"
    
              + "  <build>"
              + "    <pluginManagement>"
              + "      <plugins>"
              + "      </plugins>"
              + "    </pluginManagement>"
              + "  </build>"

              + "</project>";
    
        final MavenProject project = parse(pomString);
        
        final MavenBuild build = project.getCommon().getBuild();
        
        assertThat(build.getPluginManagement()).isNotNull();
        assertThat(build.getPluginManagement().getPlugins()).isEmpty();
    }
}
