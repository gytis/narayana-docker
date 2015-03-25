package io.narayana.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public abstract class AbstractNameServerTest {

    protected static final String JACORB_VERSION = "2.3.1";

    protected static final String DOCKER_DAEMON_URL = "http://127.0.0.1:2375";

    protected static final int NAME_SERVER_PORT = 3528;

    protected static final DockerClient DOCKER_CLIENT = DockerClientBuilder.getInstance(DOCKER_DAEMON_URL).build();

    protected static CreateContainerResponse CONTAINER;

    @BeforeClass
    public static void beforeClass() throws IOException {
        buildDockerImage();
        CONTAINER = DOCKER_CLIENT.createContainerCmd("name-server").exec();
    }

    @AfterClass
    public static void afterClass() {
        if (CONTAINER != null) {
            DOCKER_CLIENT.removeContainerCmd(CONTAINER.getId()).exec();
            CONTAINER = null;
        }
    }

    @Before
    public void before() {
        final Ports.Binding binding = Ports.Binding(NAME_SERVER_PORT);

        final PortBinding portBinding = new PortBinding(binding, ExposedPort.tcp(NAME_SERVER_PORT));
        DOCKER_CLIENT.startContainerCmd(CONTAINER.getId()).withPortBindings(portBinding).withBinds().exec();
    }

    @After
    public void after() {
        DOCKER_CLIENT.stopContainerCmd(CONTAINER.getId()).exec();
    }

    protected String getNameServiceUrl() {
        final InspectContainerResponse response = DOCKER_CLIENT.inspectContainerCmd(CONTAINER.getId()).exec();

        return "corbaloc::" + response.getNetworkSettings().getIpAddress() + ":" + NAME_SERVER_PORT
                + "/StandardNS/NameServer-POA/_root";
    }

    private static void buildDockerImage() throws IOException {
        final File baseDir = new File("./");
        final StringWriter logWriter = new StringWriter();
        final InputStream response;

        try {
            response = DOCKER_CLIENT.buildImageCmd(baseDir).exec();
        } catch (final Exception e) {
            throw new RuntimeException("Failed to create docker image. Make sure you have docker daemon running with "
                    + "\"-H " + DOCKER_DAEMON_URL + "\".", e);
        }

        try {
            final LineIterator iterator = IOUtils.lineIterator(response, "UTF-8");
            while (iterator.hasNext()) {
                String line = iterator.next();
                logWriter.write(line);
                System.out.println(line);
            }
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

}
