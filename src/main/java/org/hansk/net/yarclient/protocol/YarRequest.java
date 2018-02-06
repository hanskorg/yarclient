package org.hansk.net.yarclient.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guohao on 2018/1/31.
 */
public class YarRequest {
    private long id;
    private String method;
    private Object[] parameters;
    private String packMethod = "PHP";

    public long getId() {
        return id;
    }

    public YarRequest setId(long id) {
        this.id = id;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public YarRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public YarRequest setParameters(Object[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public String getPackMethod() {
        return packMethod;

    }

    public YarRequest setPackMethod(String packMethod) {
        this.packMethod = packMethod;
        return this;
    }



    public Map<String,Object> formatBody(){
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("i", this.getId());
        requestBody.put("m", this.getMethod());
        requestBody.put("p", this.getParameters());
        return requestBody;
    }


}
