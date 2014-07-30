package com.hackathon.izitaxed.app;

import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QuerySubmit extends AsyncTask<Object, Void, Integer>
{
    String json;
    TextView submitView;

    @Override
    protected Integer doInBackground(Object... params)
    {
        this.json = (String) params[0];
        this.submitView = (TextView) params[1];

        BufferedReader inBuffer = null;
        String url = "http://185.40.9.188:9110/DVLA/rest/postReport";
        //String url = "http://posttestserver.com/post.php";
        Integer result;
        Integer httpCode=500;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);

            StringEntity se = new StringEntity(this.json);

            request.setEntity(se);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpClient.execute(request);
            System.out.println(this.json);

            inBuffer = new BufferedReader(
                    new InputStreamReader(
                            httpResponse.getEntity().getContent()));
            StringBuffer stringBuffer = new StringBuffer("");
            String line = "";
            String newLine = System.getProperty("line.separator");
            while ((line = inBuffer.readLine()) != null) {
                stringBuffer.append(line + newLine);
            }
            inBuffer.close();

            System.out.println(stringBuffer.toString());





            httpCode = httpResponse.getStatusLine().getStatusCode();


        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (inBuffer != null) {
                try {
                    inBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpCode;
    }

    protected void onPostExecute(Integer result) {
        if(result == 200) {
            submitView.setVisibility(View.VISIBLE);
        }
    }


}
