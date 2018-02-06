package org.hansk.net.yarclient.protocol;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guohao on 2018/1/31.
 */
public interface Packager {

    public byte[] pack(YarRequest request);

    public YarResponse unpack(byte[] bytes) throws UnsupportedEncodingException;

    public String getPackagerName();

}

