package org.hansk.net.yarclient.handler;

import org.hansk.net.yarclient.client.YarClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by guohao on 2018/1/31.
 */
public class YarHandler implements InvocationHandler {

    private YarClient yarClient ;
    public YarHandler(YarClient yarClient){
        this.yarClient = yarClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return yarClient.invoke(method.getName(), args);
    }
}
