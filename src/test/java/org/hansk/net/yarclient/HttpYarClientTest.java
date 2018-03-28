package org.hansk.net.yarclient;

import org.hansk.net.yarclient.client.HttpYarClient;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by guohao on 2018/3/28.
 */
public class HttpYarClientTest {

    private interface API{
        public String test(String a,String option);
    }


    @Test
    public void call() throws Exception {
            HttpYarClient yarClient = new HttpYarClient("http://10limi.com/rpc.php",3000,3000,"json");
        API object = (API) yarClient.call(API.class);
//            Assert.assertNotEquals(object.test("1"),"1");
            String ret = object.test("1","2");
            System.out.println(ret);

    }

}