package org.hansk.net.yarclient.protocol.impl;

import org.hansk.net.yarclient.protocol.Packager;
import org.hansk.net.yarclient.protocol.YarRequest;
import org.hansk.net.yarclient.protocol.YarResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by guohao on 2018/2/5.
 */
public class MsgPackPackager implements Packager {
    @Override
    public byte[] pack(YarRequest request) {

        return new byte[0];
    }

    @Override
    public YarResponse unpack(byte[] bytes) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public String getPackagerName() {
        return "msgpack";
    }
}
