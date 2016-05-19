package com.synapse.thumbimpression.rest;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.synapse.thumbimpression.utill.AppConstants;

import java.io.ByteArrayOutputStream;

/**
 * Created by bahl on 5/3/2016.
 */
public class ThumbVerificationTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private Bitmap thumbBitmap;
    private String enchodedString = "";
    private RequestParams params = new RequestParams();
    private ProgressDialog pd;

    public ThumbVerificationTask(Context mCOntext, Bitmap thumbBitmap) {
        this.mContext = mCOntext;
        this.thumbBitmap = thumbBitmap;

        pd = new ProgressDialog(this.mContext);
        pd.setTitle("Verifying Thumb");
        pd.setMessage("Pleasw wait...");
        pd.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        thumbBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        enchodedString = Base64.encodeToString(byte_arr, 0);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        params.put("image", enchodedString);

        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post(AppConstants.END_POINT,
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
//                        prgDialog.hide();
                        Toast.makeText(mContext, response,
                                Toast.LENGTH_LONG).show();

                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
//                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(mContext,
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(mContext,
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    mContext,
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });

        pd.dismiss();
    }
}
