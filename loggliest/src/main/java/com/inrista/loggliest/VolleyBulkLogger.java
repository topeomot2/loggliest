package com.inrista.loggliest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Temitope on 9/11/2015.
 */
public class VolleyBulkLogger implements BulkLogger {


    private static RequestQueue mQueue;
    private static String url;

    public Map<String, File> mFileList = new HashMap<>();

    public VolleyBulkLogger(RequestQueue queue,String logglyUrl){
        mQueue = queue;

        if(logglyUrl != null && !logglyUrl.isEmpty())
            url = logglyUrl;
    }

    @Override
    public void setLogglyUrl(String logglyUrl) {
        if(logglyUrl != null && !logglyUrl.isEmpty())
            url = logglyUrl;
    }

    @Override
    public int log(String token, String logglyTag, File dir) {

        int i= 0;

        for (File logFile : dir.listFiles()) {

            mFileList.put("xfile"+i, logFile);
            i += 1;

            StringBuilder builder = new StringBuilder();

            try {
                // Read log files and build body of newline-separated json log entries
                FileInputStream fis = new FileInputStream(logFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append('\n');
                }
                reader.close();
            } catch (FileNotFoundException e) {
                return 0;
            } catch (IOException e) {
                return 0;
            }

            String json = builder.toString();
            if (json.isEmpty())
                return 0;


            String mUrl = url + "/bulk/"+token+"/tag/"+logglyTag;

            final LogglyJSONObjectRequest bulkRequest = new LogglyJSONObjectRequest(Request.Method.POST, mUrl, json, logFile,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Loggly.changeRecentLogFile();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );



        }
        return 0;
    }


}
