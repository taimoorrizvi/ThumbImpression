package com.example.bahl.bahl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bahl.Helper.MessageBox;
import com.example.bahl.Helper.RestFullClient;
import com.example.bahl.Model.LoginRequest;
import com.example.bahl.Model.LoginResponse;
import com.example.bahl.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sample.FpSDKSampleP41MActivity;
import com.synapse.thumbimpression.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

//import javax.xml.soap.SOAPMessage;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    final File mFile = new File("/sys/class/power_supply/usb/device/CONTROL_GPIO114");
    private Button btnLogin;
    private EditText txtUserName, txtPassword;
    private ProgressDialog loginProgressDialog;
    private LoginWorker loginWorker;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loginProgressDialog!=null){
            loginProgressDialog.dismiss();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();

        if (loginWorker != null) {
            if (loginWorker.getStatus() == AsyncTask.Status.PENDING || loginWorker.getStatus() == AsyncTask.Status.RUNNING) {
                loginWorker.cancel(false);
                loginWorker = null;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        txtUserName = (EditText) findViewById(R.id.txtLogin);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //Comment the lines below
        txtUserName.setText("fieldagent");
        txtPassword.setText("Login.123");

//        txtUserName.setText("zareen");
//        txtPassword.setText("administrator");

        openVoltage();

        btnLogin.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                String userId = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();

                if (userId.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                    MessageBox.Show(LoginActivity.this, "Warning", getString(R.string.authentication_error));
                } else {
                    loginProgressDialog = ProgressDialog.show(LoginActivity.this, "Authentication ...", "Please wait ...", true);
                    loginWorker = new LoginWorker(userId, password);
                    loginWorker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                //  new RestClient(this).execute(userId, password);
                break;

        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
       // openVoltage();
    }

    public void openVoltage() {
        try {
            FileReader inCmd = new FileReader(mFile);
            int i = inCmd.read();
            inCmd.close();
        } catch (Exception e) {
            Log.e("wuyb", "wuyb--open--write error");
        }
    }

    private class LoginWorker extends AsyncTask<Void, Void, LoginResponse> {

        private String UserName = "";
        private String passWord = "";

        public LoginWorker(String userName, String passWord) {
            this.UserName = userName;
            this.passWord = passWord;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected LoginResponse doInBackground(Void... params) {

            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();

                String serverURL = AppConstants.AUTHENTICATION_WS_LOGIN_URL;
                LoginRequest request = new LoginRequest();
                request.UserName = UserName;
                request.Password = passWord;
                String body = gson.toJson(request);
                JSONObject json = new RestFullClient().Post(serverURL, body);
                if (json != null) {
                    LoginResponse response = gson.fromJson(json.toString(), LoginResponse.class);
                    return response;
                }
            } catch (JSONException e) {
                if(loginProgressDialog!=null)
                    loginProgressDialog.dismiss();
                e.printStackTrace();

            } catch (IOException e) {
                if(loginProgressDialog!=null)
                    loginProgressDialog.dismiss();
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(final LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);

            if (loginResponse != null && loginResponse.UserId > 0) {
               // openVoltage();
                String loginJson = (new GsonBuilder()).create().toJson(loginResponse);
                SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.KEY_USER_SESSION_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(AppConstants.KEY_USER_SESSION, loginJson);
                editor.commit();
                Intent it = new Intent(getApplicationContext(), FpSDKSampleP41MActivity.class);
                startActivity(it);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

             /*  TimerTask timertask = new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
 Intent it = new Intent(getApplicationContext(), FpSDKSampleP41MActivity.class);


                startActivity(it);
                finish();

                       overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                };
                Timer timer = new Timer();
                timer.schedule(timertask, 5000);*/
            }
            else {

                try {
                    if(loginProgressDialog!=null){
                        loginProgressDialog.dismiss();
                    }
                    MessageBox.Show(LoginActivity.this, getString(R.string.login_failed_title), getString(R.string.login_failed_message));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
