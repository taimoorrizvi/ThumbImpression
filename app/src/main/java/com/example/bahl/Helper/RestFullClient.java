package com.example.bahl.Helper;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by bahl on 4/29/2016.
 */
public class RestFullClient {

    final int CONNECTION_TIMEOUT = 20000;
    final int DATARETRIEVAL_TIMEOUT = 20000;
    HttpURLConnection connection = null;



    public static final String c = RestFullClient.class.getSimpleName();

    public RestFullClient(){
        System.setProperty("http.keepAlive", "false");
    }

    public String Get(String serverURL) throws IOException, JSONException {

        URL url = new URL(serverURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
        int responseCode = connection.getResponseCode();

      /*  String userCredentials = "username:password";
        String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(),Base64.DEFAULT));
        connection.setRequestProperty ("Authorization", basicAuth);*/


        StringBuffer buffer = new StringBuffer();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        }
        else {

        }

        return buffer.toString();

    }

    public JSONObject Post(String serverURL, String body) throws IOException, JSONException {


        try {


            URL url = new URL(serverURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
            connection.setReadTimeout(CONNECTION_TIMEOUT);
            connection.setRequestMethod("POST");


            connection.setDoOutput(true);


            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(body);
            writer.flush();
            writer.close();
            os.close();


            int responseCode = connection.getResponseCode();
            StringBuffer buffer = new StringBuffer();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
            }
            else {

return null;
            }

            return new JSONObject(buffer.toString());
        } catch (Exception ex) {

            throw ex;
        } finally {

            connection.disconnect();
        }
    }

  /*  public <T> T mymethod(String input)
    {


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        List<T> list = new ArrayList<T>();
        posts = Arrays.asList(gson.fromJson(input, list.class));
    }*/
}