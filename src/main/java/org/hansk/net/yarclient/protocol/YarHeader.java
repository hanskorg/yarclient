package org.hansk.net.yarclient.protocol;

import java.util.Arrays;

/**
 * Created by guohao on 2018/1/31.
 */
/*
typedef struct _yar_header {
    unsigned int   id;            // transaction id
    unsigned short version;       // protocl version
    unsigned int   magic_num;     // default is: 0x80DFEC60
    unsigned int   reserved;
    unsigned char  provider[32];  // reqeust from who
    unsigned char  token[32];     // request token, used for authentication
    unsigned int   body_len;      // request body len
}
 */
public class YarHeader {
    private int id;
    private short version = 10;
    private int magicNumber = 0x80DFEC60;
    private int reserved;
    private byte[] provider = new byte[32];
    private byte[] token = new byte[32];
    private int bodyLength;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public byte[] getProvider() {
        return provider;
    }

    public void setProvider(byte[] provider) {
        this.provider = Arrays.copyOf(provider,32);
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = Arrays.copyOf(token,32);
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }
}
