package com.hackathon.izitaxed.app;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.entity.StringEntity;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONObject;

        import android.util.Log;
        import android.view.View;
        import android.widget.FrameLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import android.os.AsyncTask;

public class QueryRequest extends AsyncTask<Object, Void, String>
{
    TextView checkTitle;
    TextView checkBox1;
    RelativeLayout checkButton;
    RelativeLayout vehDetail;
    TextView taxed;
    TextView unTaxed;
    TextView notFound;
    TextView checkThanks;
    TextView checkNowSubmit;
    RelativeLayout continueButton;
    TextView vrm_box;
    TextView make;
    TextView model;
    TextView color;

    @Override
    protected String doInBackground(Object... params)
    {
        this.checkTitle = (TextView) params[0];
        this.checkBox1 = (TextView) params[1];
        this.checkButton = (RelativeLayout) params[2];
        this.vehDetail = (RelativeLayout) params[3];
        this.taxed = (TextView) params[4];
        this.unTaxed = (TextView) params[5];
        this.notFound = (TextView) params[6];
        this.checkThanks = (TextView) params[7];
        this.checkNowSubmit = (TextView) params[8];
        this.continueButton = (RelativeLayout) params[9];
        this.vrm_box = (TextView) params[10];
        this.make = (TextView) params[11];
        this.model = (TextView) params[12];
        this.color = (TextView) params[13];
        String vrm = (String) params[14];


        BufferedReader inBuffer = null;
        String url = "http://185.40.9.188:9110/DVLA/rest/colour";
        //String url = "http://posttestserver.com/post.php";
        String result;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("VRM", vrm);

            StringEntity se = new StringEntity(jsonObj.toString());
            System.out.println("JS " + jsonObj.toString());

            request.setEntity(se);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpClient.execute(request);
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

            //JSONObject json = new JSONObject(stringBuffer.toString());
            //result = json.toString();
            result = stringBuffer.toString();

        } catch(Exception e) {
            result = e.getMessage();
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
        return result;
    }

    protected void onPostExecute(String result)
    {
        try {
            checkButton.setVisibility(View.GONE);
            checkBox1.setVisibility(View.GONE);
            checkTitle.setVisibility(View.GONE);
            vrm_box.setEnabled(false);
            boolean checkSorted = false;

            JSONObject jResult = new JSONObject(result);
            if(jResult.has("notFound")==true && jResult.getBoolean("notFound")==true) {
                notFound.setVisibility(View.VISIBLE);
                checkThanks.setVisibility(View.VISIBLE);
                checkSorted = true;
            } else {

                make.setText(jResult.getString("make"));
                model.setText(jResult.getString("model"));
                color.setText(jResult.getString("colour"));
                vehDetail.setVisibility(View.VISIBLE);

                if (jResult.has("isTaxed") == true) {
                    if (jResult.getBoolean("isTaxed") == true) {
                        taxed.setVisibility(View.VISIBLE);
                        checkThanks.setVisibility(View.VISIBLE);
                        checkSorted = true;
                    }
                }
                if (jResult.has("isSorned") == true) {
                    if (jResult.getBoolean("isSorned") == true) {
                        unTaxed.setVisibility(View.VISIBLE);
                        checkNowSubmit.setVisibility(View.VISIBLE);
                        continueButton.setVisibility(View.VISIBLE);
                        checkSorted = true;
                    }
                }

                if(checkSorted == false) {
                    unTaxed.setVisibility(View.VISIBLE);
                    checkNowSubmit.setVisibility(View.VISIBLE);
                    continueButton.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
