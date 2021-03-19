package com.example.pokechallange.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ResponseHandlerCustom implements ResponseHandler<String> {

    private String encoding="UTF-8";


    public String handleResponse(
            final HttpResponse response) throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 303) {
            HttpEntity entity = response.getEntity();

            if(this.encoding==null){
                return entity != null ? EntityUtils.toString(entity) : null;
            }else{
                return entity != null ? EntityUtils.toString(entity,this.encoding) : null;
            }
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }
}
