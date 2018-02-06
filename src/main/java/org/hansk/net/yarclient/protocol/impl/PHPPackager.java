package org.hansk.net.yarclient.protocol.impl;

import de.ailis.pherialize.MixedArray;
import de.ailis.pherialize.Pherialize;
import org.hansk.net.yarclient.protocol.Packager;
import org.hansk.net.yarclient.protocol.YarRequest;
import org.hansk.net.yarclient.protocol.YarResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by guohao on 2018/2/5.
 */
public class PHPPackager implements Packager {
    @Override
    public byte[] pack(YarRequest request) {
        return Pherialize.serialize(request.formatBody()).getBytes();
    }

    @Override
    public YarResponse unpack(byte[] bytes) throws UnsupportedEncodingException {
        MixedArray ret = Pherialize.unserialize(new String(bytes)).toArray();
        YarResponse response = new YarResponse();
        response.setId(ret.getLong("i"));
        response.setStatus(ret.getInt("s"));
        response.setReturnValue(ret.getMixed("r"));
        if(ret.contains("o")){
            response.setOutput(ret.getString("o"));
        }
        if(ret.contains("e")){
            response.setError(ret.getString("e"));
        }
        return response;
    }

    @Override
    public String getPackagerName() {
        return "php";
    }
}
