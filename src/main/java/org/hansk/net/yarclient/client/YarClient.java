package org.hansk.net.yarclient.client;

import org.hansk.net.yarclient.protocol.YarDecodeException;

/**
 * Created by guohao on 2018/1/31.
 */
public interface YarClient {

    public Object invoke(String method,Object[] args) throws YarDecodeException ;

}
