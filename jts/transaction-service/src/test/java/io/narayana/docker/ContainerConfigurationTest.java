package io.narayana.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.api.model.ExposedPort;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Need to start docker daemon with: sudo docker -H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock -d
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class ContainerConfigurationTest extends AbstractTransactionServiceTest {

    private static final String NARAYANA_VERSION = "5.0.5.Final-SNAPSHOT";

    private static final String JACORB_LOG_LEVEL = "3";

    @Test
    public void testConfiguration() {
        final InspectContainerResponse response = DOCKER_CLIENT.inspectContainerCmd(CONTAINER.getId()).exec();
        final ContainerConfig config = response.getConfig();

        validateEnvironmentVariables(config.getEnv());
        validateStartupCommand(config.getCmd());

        Assert.assertEquals("/home", config.getWorkingDir());
        Assert.assertArrayEquals(new String[]{"/home/docker-entrypoint.sh"}, config.getEntrypoint());
        Assert.assertArrayEquals(new ExposedPort[]{ExposedPort.tcp(TRANSACTION_SERVICE_PORT)}, config.getExposedPorts());
    }

    private void validateEnvironmentVariables(final String[] environmentVariablesArray) {
        Assert.assertEquals("Invalid number of environment variables", 6, environmentVariablesArray.length);

        final List<String> environmentVariables = Arrays.asList(environmentVariablesArray);

        final int narayanaPortIndex = environmentVariables.indexOf("NARAYANA_PORT=" + TRANSACTION_SERVICE_PORT);
        Assert.assertTrue("NARAYANA_PORT environment variable is missing", narayanaPortIndex > -1);

        final int narayanaVersionIndex = environmentVariables.indexOf("NARAYANA_VERSION=" + NARAYANA_VERSION);
        Assert.assertTrue("NARAYANA_VERSION environment variable is missing", narayanaVersionIndex > -1);

        final int narayanaHomeIndex = environmentVariables.indexOf("NARAYANA_HOME=/home/narayana");
        Assert.assertTrue("NARAYANA_HOME environment variable is missing", narayanaHomeIndex > -1);

        final int javaHomeIndex = environmentVariables.indexOf("JAVA_HOME=/etc/alternatives/jre");
        Assert.assertTrue("JAVA_HOME environment variable is missing", javaHomeIndex > -1);

        final int jacorbLogLevelIndex = environmentVariables.indexOf("JACORB_LOG_LEVEL=" + JACORB_LOG_LEVEL);
        Assert.assertTrue("JACORB_LOG_LEVEL environment variable is missing", jacorbLogLevelIndex > -1);
    }

    private void validateStartupCommand(final String[] command) {
        Assert.assertEquals("Invalid size of the command", 3, command.length);
        Assert.assertEquals("/bin/sh is an expected executable", "/bin/sh", command[0]);
        Assert.assertEquals("-c is an expected attribute", "-c", command[1]);

        final String[] transactionServiceCommand = command[2].split("\\s+");
        Assert.assertEquals("Invalid size of the transacion service command", 12, transactionServiceCommand.length);
        Assert.assertEquals("source", transactionServiceCommand[0]);
        Assert.assertEquals("$NARAYANA_HOME/jts-jacorb-setup-env.sh;", transactionServiceCommand[1]);
        Assert.assertEquals("eval", transactionServiceCommand[2]);
        Assert.assertEquals("java", transactionServiceCommand[3]);
        Assert.assertEquals("-Dcom.arjuna.orbportability.common.OrbPortabilityEnvironmentBean.bindMechanism=NAME_SERVICE",
                transactionServiceCommand[4]);
        Assert.assertEquals("-DObjectStoreEnvironmentBean.objectStoreDir=/home/tx-object-store", transactionServiceCommand[5]);
        Assert.assertEquals("$NARAYANA_OPTS", transactionServiceCommand[6]);
        Assert.assertEquals("-DOAPort=$NARAYANA_PORT", transactionServiceCommand[7]);
        Assert.assertEquals("-Djacorb.log.default.verbosity=$JACORB_LOG_LEVEL", transactionServiceCommand[8]);
        Assert.assertEquals("com.arjuna.ats.jts.TransactionServer", transactionServiceCommand[9]);
        Assert.assertEquals("-recovery", transactionServiceCommand[10]);
        Assert.assertEquals("-ORBInitRef.NameService=$NAME_SERVER_URL", transactionServiceCommand[11]);
    }

}
