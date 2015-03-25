package io.narayana.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.api.model.ExposedPort;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Need to start docker daemon with: sudo docker -H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock -d
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class ContainerConfigurationTest extends AbstractNameServerTest {

    @Test
    public void testConfiguration() {
        final InspectContainerResponse response = DOCKER_CLIENT.inspectContainerCmd(CONTAINER.getId()).exec();
        final ContainerConfig config = response.getConfig();

        validateEnvironmentVariables(config.getEnv());
        validateStartupCommand(config.getCmd());

        Assert.assertEquals("/home", config.getWorkingDir());
        Assert.assertArrayEquals(new String[]{"/home/docker-entrypoint.sh"}, config.getEntrypoint());
        Assert.assertArrayEquals(new ExposedPort[]{ExposedPort.tcp(NAME_SERVER_PORT)}, config.getExposedPorts());
    }

    private void validateEnvironmentVariables(final String[] environmentVariablesArray) {
        Assert.assertEquals("Invalid number of environment variables", 4, environmentVariablesArray.length);

        // Need this double copy to make list mutable
        final List<String> environmentVariables = new ArrayList<String>(Arrays.asList(environmentVariablesArray));

        final int nameServerPortIndex = environmentVariables.indexOf("NAME_SERVER_PORT=" + NAME_SERVER_PORT);
        Assert.assertTrue("NAME_SERVER_PORT environment variable is missing", nameServerPortIndex > -1);
        environmentVariables.remove(nameServerPortIndex);

        final int jacorbVersionIndex = environmentVariables.indexOf("JACORB_VERSION=" + JACORB_VERSION);
        Assert.assertTrue("JACORB_VERSION environment variable is missing", jacorbVersionIndex > -1);
        environmentVariables.remove(jacorbVersionIndex);

        final int jacorbHomeIndex = environmentVariables.indexOf("JACORB_HOME=/home/jacorb-" + JACORB_VERSION);
        Assert.assertTrue("JACORB_HOME environment variable is missing", jacorbHomeIndex > -1);
        environmentVariables.remove(jacorbHomeIndex);

        final String path = environmentVariables.get(0);
        System.out.println(path);
        Assert.assertTrue("JacORB is not in the PATh", path.contains("/home/jacorb-" + JACORB_VERSION + "/bin"));
    }

    private void validateStartupCommand(final String[] command) {
        Assert.assertEquals("Invalid size of the command", 3, command.length);
        Assert.assertEquals("/bin/sh is an expected executable", "/bin/sh", command[0]);
        Assert.assertEquals("-c is an expected attribute", "-c", command[1]);
        Assert.assertTrue("Name service command should start with ns", command[2].startsWith("ns"));
        Assert.assertTrue("OAPort should be passed to the name service command",
                command[2].contains("-DOAPort=$NAME_SERVER_PORT"));
        Assert.assertTrue("IOR file name should be passed to the name service command",
                command[2].contains("-Djacorb.naming.ior_filename=/home/NS_Ref"));
    }

}
