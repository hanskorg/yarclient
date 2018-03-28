package org.hansk.net.yarclient.client;

import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.hansk.net.yarclient.handler.YarHandler;
import org.hansk.net.yarclient.protocol.YarDecodeException;
import org.hansk.net.yarclient.protocol.YarProtocol;
import org.hansk.net.yarclient.protocol.YarRequest;
import org.hansk.net.yarclient.protocol.YarResponse;
import org.hansk.net.yarclient.protocol.impl.JsonPackager;
import org.hansk.net.yarclient.protocol.impl.MsgPackPackager;
import org.hansk.net.yarclient.protocol.impl.PHPPackager;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.lang.reflect.Proxy;


/**
 * Created by guohao on 2018/2/6.
 */
public class HttpYarClient implements YarClient {

    private HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

        @Override
        public HttpMessageParser<HttpResponse> create(
                SessionInputBuffer buffer, MessageConstraints constraints) {
            LineParser lineParser = new BasicLineParser() {

                @Override
                public Header parseHeader(final CharArrayBuffer buffer) {
                    try {
                        return super.parseHeader(buffer);
                    } catch (ParseException ex) {
                        return new BasicHeader(buffer.toString(), null);
                    }
                }

            };
            return new DefaultHttpResponseParser(
                    buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

                @Override
                protected boolean reject(final CharArrayBuffer line, int count) {
                    return false;
                }

            };
        }

    };
    HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
    HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
            requestWriterFactory, responseParserFactory);
    SSLContext sslcontext = SSLContexts.createSystemDefault();
    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext))
            .build();

    private final HttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(
            socketFactoryRegistry, connFactory);

    private final CloseableHttpClient httpClient;
    private final YarProtocol yarProtocol;
    private final String yarUrl;
    private final String packageType;
    public HttpYarClient(String yarUrl, int connectTimeout, int requestTimeout, String packageType){
        this.yarUrl       = yarUrl;
        this.packageType = packageType;
        int packageTypeInt = packageType.equals("json") ? 1 : (packageType.equals("php") ? 2 : 3);
        switch (packageTypeInt){
            case 1:
                yarProtocol = new YarProtocol("token",new JsonPackager());
                break;
            case 2:
                yarProtocol = new YarProtocol("token",new PHPPackager());
                break;
            case 3:
                yarProtocol = new YarProtocol("token",new MsgPackPackager());
                break;
            default:
                yarProtocol = new YarProtocol("token",new JsonPackager());

        }
        httpClient =  HttpClients.custom().setConnectionManager(httpClientConnectionManager)
                .build();
    }

    public Object call(Class service){
        YarHandler handler = new YarHandler(this);
        if (service.isInterface()) {
            return Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, handler);
        } else {
            return Proxy.newProxyInstance(service.getClassLoader(), service.getInterfaces(), handler);
        }
    }


    public Object invoke(String method,Object[] args) throws YarDecodeException {

        YarResponse yarResponse = null;

        YarRequest yarRequest = new YarRequest();
        yarRequest.setMethod(method)
                .setParameters(args)
                .setPackMethod(packageType);

        try {
            HttpPost httpPost = new HttpPost(yarUrl);
            HttpEntity httpEntity = new ByteArrayEntity(yarProtocol.encode(yarRequest), ContentType.APPLICATION_FORM_URLENCODED);
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200){
                yarResponse = yarProtocol.decode(EntityUtils.toByteArray(response.getEntity()));
            }else{
                throw new YarDecodeException("http error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return yarResponse.getReturnValue();

    }


}
