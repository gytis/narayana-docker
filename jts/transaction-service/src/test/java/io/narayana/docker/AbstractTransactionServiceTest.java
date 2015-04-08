package io.narayana.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
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
public abstract class AbstractTransactionServiceTest {

    private static final String CONTAINER_NAME = "transaction-service";

    private static final String DOCKER_DAEMON_URL = "http://127.0.0.1:2375";

    protected static final int TRANSACTION_SERVICE_PORT = 4710;

    protected static final DockerClient DOCKER_CLIENT = DockerClientBuilder.getInstance(DOCKER_DAEMON_URL).build();

    protected static CreateContainerResponse CONTAINER;

    @BeforeClass
    public static void beforeClass() throws IOException {
        buildDockerImage();
        CONTAINER = DOCKER_CLIENT.createContainerCmd(CONTAINER_NAME).exec();
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
        final Ports.Binding binding = Ports.Binding(TRANSACTION_SERVICE_PORT);

        final PortBinding portBinding = new PortBinding(binding, ExposedPort.tcp(TRANSACTION_SERVICE_PORT));
        DOCKER_CLIENT.startContainerCmd(CONTAINER.getId()).withPortBindings(portBinding).withBinds().exec();
    }

    @After
    public void after() {
        DOCKER_CLIENT.stopContainerCmd(CONTAINER.getId()).exec();
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
