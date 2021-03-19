package com.example.pokechallange.http;


import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import java.security.cert.X509Certificate;
import java.util.List;

public class WebClient {
    protected HttpClientContext context;
    protected CloseableHttpClient httpclient;
    protected String encodingResponse;

    public WebClient() throws Exception{
        httpclient=WebClient.createClient();
        this.context = HttpClientContext.create();
    }

    public  static CloseableHttpClient createClient() throws Exception{
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpclient = HttpClients.custom().
                setDefaultRequestConfig(config)
                .build();
        return httpclient;
    }

    //devuelve el content desde una URL, deberia ser el unico meto que usa URLs
    public String getContentFromUrl(String urlS)throws Exception{
        HttpGet httpget = new HttpGet(urlS);
        System.out.println("Executing request " + httpget.getRequestLine());
        String responseBody=httpclient.execute(httpget, new ResponseHandlerCustom());


        return responseBody;

    }





}
