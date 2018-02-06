package org.hansk.net.yarclient.protocol;

/**
 * Created by guohao on 2018/1/31.
 */
public class YarResponse {
    /**
     * 请求id
     */
    private long id;
    /**
     * 响应状态
     */
    private int  status;
    /**
     * 返回对象
     */
    private Object returnValue;

    private String output;
    private String error;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
