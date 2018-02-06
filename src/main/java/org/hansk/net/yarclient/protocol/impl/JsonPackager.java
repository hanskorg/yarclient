package org.hansk.net.yarclient.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.hansk.net.yarclient.protocol.Packager;
import org.hansk.net.yarclient.protocol.YarRequest;
import org.hansk.net.yarclient.protocol.YarResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by guohao on 2018/2/1.
 */
public class JsonPackager implements Packager {
    @Override
    public byte[] pack(YarRequest request) {
        JSONObject jsonObject = new JSONObject(request.formatBody());
        return jsonObject.toJSONString().getBytes();
    }

    @Override
    public YarResponse unpack(byte[] bytes) throws UnsupportedEncodingException {
        JSON json = JSON.parseObject(new String(bytes,"UTF-8"));
        return json.toJavaObject(YarResponse.class);
    }

    @Override
    public String getPackagerName() {
        return "json";
    }
}
