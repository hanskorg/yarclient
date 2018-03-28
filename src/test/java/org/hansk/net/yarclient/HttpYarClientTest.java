package org.hansk.net.yarclient;

import org.hansk.net.yarclient.client.HttpYarClient;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by guohao on 2018/3/28.
 */
public class HttpYarClientTest {

    private interface TestA{
        public void test(String a);
    }


    @Test
    public void call() throws Exception {
            HttpYarClient yarClient = new HttpYarClient("http://domain/rpc/test",3000,3000,"json");
            TestA object = (TestA) yarClient.call(TestA.class);
//            Assert.assertNotEquals(object.test("1"),"1");
            object.test("1");
    }

}