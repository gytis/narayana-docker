package io.narayana.docker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * Need to start docker daemon with: sudo docker -H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock -d
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class NameServiceTest extends AbstractNameServerTest {

    @Before
    public void before() {
        super.before();

        try {
            // Even though the container has started, we need to give some time for the name service to start
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testNameService() throws InvalidName, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound {
        final String[] args = getOrbArgs();
        final ORB orb = ORB.init(args, null);

        final org.omg.CORBA.Object object = orb.resolve_initial_references("NameService");
        Assert.assertNotNull("NameService reference was not resolved", object);

        final NamingContextExt rootContext = NamingContextExtHelper.narrow(object);
        Assert.assertNotNull("Root context was not narrowed", rootContext);
    }

    private String[] getOrbArgs() {
        final String nameServiceUrl = getNameServiceUrl();

        return new String[] {
                "-ORBInitRef",
                "NameService=" + nameServiceUrl
        };
    }

}
