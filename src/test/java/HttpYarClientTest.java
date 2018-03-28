import org.hansk.net.yarclient.client.HttpYarClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by guohao on 2018/3/28.
 */
@RunWith(Arquillian.class)
public class HttpYarClientTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(HttpYarClient.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
