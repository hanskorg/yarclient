package org.hansk.net.yarclient.protocol;

import org.hansk.net.yarclient.protocol.impl.PHPPackager;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guohao on 2018/1/31.
 * unsigned int   id;            // transaction id
 unsigned short version;       // protocl version
 unsigned int   magic_num;     // default is: 0x80DFEC60
 unsigned int   reserved;
 unsigned char  provider[32];  // reqeust from who
 unsigned char  token[32];     // request token, used for authentication
 unsigned int   body_len;      // request body len
 */
public class YarProtocol {

    private short version = 10;
    private String provider = "org.hansk.net.yarclient";
    private String token = "a47a5e87c1fbec33f53a3b844671816a";

    private Packager packager = new PHPPackager();

    private GenerateId generateId = new GenerateId();

    public YarProtocol(String token, Packager packager){
        this.token    = token;
        this.packager = packager;
    }

    private YarHeader makeHeader(YarRequest request){
        YarHeader yarHeader = new YarHeader();
        yarHeader.setId(generateId.get());
        yarHeader.setProvider(provider.getBytes());
        yarHeader.setReserved(0);
        yarHeader.setVersion(version);
        yarHeader.setToken(token.getBytes());
        byte[] bytes =  packager.pack(request);
        yarHeader.setBodyLength(bytes.length);
        return yarHeader;
    }

    /**
     *  编码
     * @param request
     * @return
     * @throws IOException
     */
    public byte[] encode(YarRequest request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOutputStream);
        YarHeader yarHeader = makeHeader(request);
        out.writeInt(yarHeader.getId());
        out.writeShort(yarHeader.getVersion());
        out.writeInt(yarHeader.getMagicNumber());
        out.writeInt(yarHeader.getReserved());
        out.write(yarHeader.getProvider(), 0, 32);
        out.write(yarHeader.getToken(), 0, 32);
        out.writeInt(yarHeader.getBodyLength());
        out.write(Arrays.copyOf(request.getPackMethod().toUpperCase().getBytes("UTF-8"),8));
        out.write(packager.pack(request));
        return byteArrayOutputStream.toByteArray();
    }

    /**
     *  解码
     * @param responseBytes 响应字节数组
     * @return YarResponse 响应
     * @throws IOException
     * @throws YarDecodeException
     */
    public YarResponse decode(byte[] responseBytes) throws IOException, YarDecodeException {
        if(responseBytes.length < 80){
            throw new YarDecodeException("bytes.length too short, 80+ need");
        }
        YarResponse yarResponse = null;
        YarHeader   yarHeader   = new YarHeader();
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(responseBytes));
        yarHeader.setId(in.readInt());
        yarHeader.setVersion(in.readShort());
        if (in.readInt() != yarHeader.getMagicNumber()) {
            throw new YarDecodeException("magic number not match!");
        }
        yarHeader.setReserved(in.readInt());

        byte[] provider = new byte[32];
        in.read(provider);
        yarHeader.setProvider(provider);

        byte[] token = new byte[32];
        in.read(token);
        yarHeader.setToken(token);

        //TODO 为什么 最后8字节空?
        yarHeader.setBodyLength(in.readInt() - 8);

        byte[] packMethodBytes  = new byte[8];
        in.read(packMethodBytes);
        for (int i =0 ; i < packMethodBytes.length ; i++){
            if(packMethodBytes[i] == 0){
                packMethodBytes = Arrays.copyOf(packMethodBytes,i );
                break;
            }
        }
        String packMethod = new String(packMethodBytes,"UTF-8");
        byte[] bodyBytes = new byte[yarHeader.getBodyLength()];
        if(in.available() > bodyBytes.length){
            throw new YarDecodeException("decode out of bound!");
        }
        in.read(bodyBytes,0,yarHeader.getBodyLength());
        yarResponse = packager.unpack(bodyBytes);
        return yarResponse;
    }
    private static class GenerateId{
        private AtomicInteger currentId = new AtomicInteger(0);

        public int get(){
            return currentId.incrementAndGet();
        }
    }
}

