package com.inrista.loggliest;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by Temitope on 9/11/2015.
 */
public class LogglyJSONObjectRequest extends JsonObjectRequest {


    public File mFile;
    private String jsonBody;

    public LogglyJSONObjectRequest(int method, String url,  String json, File nameFile,
                                   Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){

        super(method,url, json, listener,errorListener);
        mFile = nameFile;
        jsonBody = json;

    }

/*
    @Override
    public byte[] getBody() {
        return  jsonBody.getBytes(Charset.forName("UTF-8"));
    }
    */


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            mFile.delete();
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
