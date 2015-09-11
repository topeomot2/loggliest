package com.inrista.loggliest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by Temitope on 9/11/2015.
 */
public class RetrofitBulkLogger implements BulkLogger {

    private static IBulkLog mBulkLogService;
    private String lurl;

    public RetrofitBulkLogger(String logglyUrl){

        if(logglyUrl != null && !logglyUrl.isEmpty())
            lurl = logglyUrl;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(lurl)
                .build();
        mBulkLogService = restAdapter.create(IBulkLog.class);
    }



    private interface IBulkLog {
        @Headers("content-type:application/json")
        @POST("/bulk/{token}/tag/{tag}")
        Response log(@Path("token") String token, @Path("tag") String logtag, @Body TypedInput body);
    }


    @Override
    public int log(String token, String logglyTag, File dir) {

        for (File logFile : dir.listFiles()) {

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

            try {
                // Blocking POST
                TypedInput body = new TypedByteArray("application/json", json.getBytes());
                Response answer = mBulkLogService.log(token, logglyTag, body);

                if (answer.getStatus() == 200) {
                    logFile.delete();
                    Loggly.changeRecentLogFile();
                }

            }

            // Post failed for some reason, keep log files and retry later
            catch (RetrofitError error) {}


        }
        return 0;
    }

    @Override
    public void setLogglyUrl(String logglyUrl) {
        if(logglyUrl != null && !logglyUrl.isEmpty())
            lurl = logglyUrl;
    }
}
