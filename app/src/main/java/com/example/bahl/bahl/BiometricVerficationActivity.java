package com.example.bahl.bahl;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.IDWORLD.HostUsb;
import com.IDWORLD.LAPI;
import com.crashlytics.android.Crashlytics;
import com.example.bahl.Helper.MessageBox;
import com.example.bahl.Helper.RestFullClient;
import com.example.bahl.Model.BiometricVerificationDto;
import com.example.bahl.Model.LOV;
import com.example.bahl.Model.LOVModel;
import com.example.bahl.util.AppConstants;
import com.google.gson.Gson;
import com.synapse.thumbimpression.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class BiometricVerficationActivity extends AppCompatActivity {

    //------------------------------------------------------------------------------------------
    public static final int MSG_SHOW_TEXT = 101;
    public static final int MSG_SHOW_IMAGE = 102;
    public static final int MSG_VIEW_TEMPLATE_1 = 103;
    public static final int MSG_VIEW_TEMPLATE_2 = 104;
    //------------------------------------------------------------------------------------------
    public static final int BUTTONS_INITIALIZED = 0;
    public static final int BUTTONS_FINALIZED = 1;
    public static final int BUTTONS_CAPTURERIZED = 2;
    //------------------------------------------------------------------------------------------
    public static final int MESSAGE_ID_ENABLED = 403;
    public static final int BUTTONS_ID_ENABLED_FINALIZED = 404;
    public static final int BUTTONS_ID_ENABLED_INITIALIZED = 405;
    public static final int BUTTONS_ID_ENABLED_CAPTURERIZED = 406;
    final File mFile = new File("/sys/class/power_supply/usb/device/CONTROL_GPIO114");
    private final String TAG = BiometricVerficationActivity.class.getSimpleName();
    private LOVManager lovManager;
    private ImageView ivthumbImpression;
    private final Handler m_fEvent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_TEXT:
//                    tvMessage.setText((String)msg.obj);
                    break;
                case MSG_VIEW_TEMPLATE_1:
//                    tvTemp1.setText((String)msg.obj);
                    break;
                case MSG_VIEW_TEMPLATE_2:
//                    tvTemp2.setText((String)msg.obj);
                    break;
                case MESSAGE_ID_ENABLED:
                    Button btn = (Button) findViewById(msg.arg1);
                    if (msg.arg2 != 0) btn.setEnabled(true);
                    else btn.setEnabled(false);
                    break;
                case BUTTONS_ID_ENABLED_INITIALIZED:
//                    EnableBittons(BUTTONS_INITIALIZED);
                    break;
                case BUTTONS_ID_ENABLED_FINALIZED:
//                    EnableBittons(BUTTONS_FINALIZED);
                    break;
                case BUTTONS_ID_ENABLED_CAPTURERIZED:
//                    EnableBittons(BUTTONS_CAPTURERIZED);
                    break;
                case MSG_SHOW_IMAGE: {
                    ShowFingerBitmap((byte[]) msg.obj, msg.arg1, msg.arg2);
                    //Send the data for Verification here
                }
                break;
            }
        }
    };
    private ProgressDialog loginProgressDialog;
    private Button btnSave;
    private LOV selectedCustomerCategory;
    private byte[] ImageByte;
    private Bitmap thumbBitmap;
    //ThumbVerification
    //------------------------------------------------------------------------------------------
    private HostUsb m_cHost;
    private LAPI m_cLAPI;
    private int m_hUSB = 0;
    private long m_hDevice = 0;
    private byte[] m_image = new byte[LAPI.WIDTH * LAPI.HEIGHT];
    //------------------------------------------------------------------------------------------
    private byte[] m_itemplate_1 = new byte[LAPI.FPINFO_STD_MAX_SIZE];
    private byte[] m_itemplate_2 = new byte[LAPI.FPINFO_STD_MAX_SIZE];

    @Override
    protected void onPause() {
        super.onPause();
        lovManager.cancel(true);
    }

    @Override
    protected void onDestroy() {
        CLOSE_DEVICE();
        super.onDestroy();
        closeVoltage();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        openVoltage();
        openDevice();
    }

    public void openVoltage() {
        try {
            FileReader inCmd = new FileReader(mFile);
            int i = inCmd.read();
            inCmd.close();

            displayMessageOnUIThread("Voltage Status", "Opened Voltage !");

        } catch (Exception e) {
            Log.e("hm71", "wuyb--open--write error");

            displayMessageOnUIThread("Voltage Status", "Error occured while Opening Voltage !, Error = " + e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_verfication);
        loginProgressDialog = ProgressDialog.show(this, "Please wait ...", "Loading....", true);

        Fabric.with(this, new Crashlytics());
//        openVoltage();

        // Save biometric data to server
        btnSave = (Button) findViewById(R.id.btnLogin);
        if (btnSave != null) {
            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Save
                    saveBiometricVerification();
                }
            });
        }

        m_cHost = new HostUsb(BiometricVerficationActivity.this);
        m_cLAPI = new LAPI();

        //Akmal To open device for thumb impression

        //opening device initially
        // _thumbImpressionManager.OPEN_DEVICE();

        // Capture Thumb impression (On click) Akmal
        ivthumbImpression = (ImageView) findViewById(R.id.ivThumbImpression);
        ivthumbImpression.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save
//                getThumbImpression();
//                GET_IMAGE();
                Runnable r = new Runnable() {
                    public void run() {
                        GET_IMAGE();
                    }
                };
                Thread s = new Thread(r);
                s.start();
            }
        });


        Spinner spinnerCustomerCategory = (Spinner) findViewById(R.id.spinnerCustomerCategory);
        if (spinnerCustomerCategory != null) {
            spinnerCustomerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selectedCustomerCategory = (LOV) parentView.getSelectedItem();
                    String docTypeUrl = "IdentityDocType/" + selectedCustomerCategory;
                    new LOVManager(new LOVModel(R.id.spinneridentityType, docTypeUrl)).execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
        }

        // Load LOV
        // lovManager =  new LOVManager(new LOVModel(R.id.spinnerPurpose, "Purpose"));
        //lovManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new LOVManager(new LOVModel(R.id.spinnerPurpose, "Purpose")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new LOVManager(new LOVModel(R.id.spinnerCustomerCategory, "CustomerCategory")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new LOVManager(new LOVModel(R.id.spinnerContactType, "BioContract")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new LOVManager(new LOVModel(R.id.spinnerProductCategory, "ProductCategory")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    // Akmal
//    private void getThumbImpression() {
//        try {
//            Runnable r = new Runnable() {
//                public void run() {
////                    ImageDto imageDto = _thumbImpressionManager.GET_IMAGE();
//                    ImageByte = imageDto.Data;
//                    ShowFingerBitmap(imageDto.Data, imageDto.Width, imageDto.Height);
//                }
//            };
//            Thread s = new Thread(r);
//            s.start();
//        } catch (Exception ex) {
//            MessageBox.Show(getBaseContext(), "Error", ex.getMessage());
//        }
//    }

    // Akmal
    private void openDevice() {
        try {
//            Runnable r = new Runnable() {
//                public void run() {
//
//                    _thumbImpressionManager.OPEN_DEVICE();
//                }
//
//            };
//            Thread s = new Thread(r);
//            s.start();

            displayMessageOnUIThread("Device Opened Status", "Opening Device...");

            Runnable r = new Runnable() {
                public void run() {
                    OPEN_DEVICE();
                }
            };
            Thread s = new Thread(r);
            s.start();

        } catch (Exception ex) {
//            MessageBox.Show(getBaseContext(), "Error", ex.getMessage());
            displayMessageOnUIThread("Device Opened Status", "Error occured while Opening Device !, Error = " + ex.toString());
        }

    }

    private void saveBiometricVerification() {
        try {
            String serverURL = AppConstants.BIOMETRIC_WS_URL;// getString(R.string.url_save_biometric);
            EditText txtContactNo = (EditText) findViewById(R.id.txtContactNo);
            EditText txtIdentityNo = (EditText) findViewById(R.id.txtIdentityNo);
            Spinner spinnerProductCategory = (Spinner) findViewById(R.id.spinnerProductCategory);
            Spinner spinnerPurpose = (Spinner) findViewById(R.id.spinnerPurpose);
            Spinner spinnerContactType = (Spinner) findViewById(R.id.spinnerContactType);
            Spinner spinneridentityType = (Spinner) findViewById(R.id.spinneridentityType);
            short customerCategoryId = 0, productCategoryId = 0, purposeId = 0, contactTypeId = 0, identityTypeId = 0;
            if (selectedCustomerCategory != null)
                customerCategoryId = Short.parseShort(selectedCustomerCategory.LOVRow);
            if (spinnerProductCategory != null && spinnerProductCategory.getSelectedItem() != null)
                productCategoryId = Short.parseShort(((LOV) spinnerProductCategory.getSelectedItem()).LOVRow);
            if (spinnerPurpose != null && spinnerPurpose.getSelectedItem() != null)
                purposeId = Short.parseShort(((LOV) spinnerPurpose.getSelectedItem()).LOVRow);
            if (spinnerContactType != null && spinnerContactType.getSelectedItem() != null)
                contactTypeId = Short.parseShort(((LOV) spinnerContactType.getSelectedItem()).LOVRow);
            if (spinneridentityType != null && spinneridentityType.getSelectedItem() != null)
                identityTypeId = Short.parseShort(((LOV) spinneridentityType.getSelectedItem()).LOVRow);

            BiometricVerificationDto biometricVerificationDto = new BiometricVerificationDto();
            biometricVerificationDto.BiometricFingerCode = 1;
            biometricVerificationDto.ContactTypeId = contactTypeId;
            biometricVerificationDto.ContactNumber = txtContactNo != null ? txtContactNo.getText().toString() : "";
            biometricVerificationDto.CustomerCategoryId = customerCategoryId;
            // Akmal
            //biometricVerificationDto.ImagebytesString
            biometricVerificationDto.ProductCategoryId = productCategoryId;
            biometricVerificationDto.PurposeId = purposeId;
            biometricVerificationDto.UserIdentificationDocumentId = identityTypeId;
            biometricVerificationDto.UserIdentificationDocumentNo = txtIdentityNo != null ? txtIdentityNo.getText().toString() : "";

            Gson gson = new Gson();
            String biometricJson = gson.toJson(biometricVerificationDto);

            ///JSONObject  response =  new RestFullClient().Post(serverURL, biometricJson);


        } catch (Exception ex) {

        }
    }

    // to display image in imageview
    private void ShowFingerBitmap(byte[] image, int width, int height) {
        if (width == 0) return;
        if (height == 0) return;

        int[] RGBbits = new int[width * height];
//        ivthumbImpression.invalidate();
        for (int i = 0; i < width * height; i++) {
            int v;
            if (image != null) v = image[i] & 0xff;
            else v = 0;
            RGBbits[i] = Color.rgb(v, v, v);
        }

        final Bitmap thumbBitmap = Bitmap.createBitmap(RGBbits, width, height, Bitmap.Config.RGB_565);

        // thumbVerificationTask = new ThumbVerificationTask(FpSDKSampleP41MActivity.this, bmp);
        //  thumbVerificationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        displayMessageOnUIThread("Fingerprint Bitmap Data = ", "" + image);
        displayMessageOnUIThread("Fingerprint Bitmap width = ", "" + width);
        displayMessageOnUIThread("Fingerprint Bitmap height = ", "" + height);
        displayMessageOnUIThread("Fingerprint Bitmap = ", "" + thumbBitmap);

        ivthumbImpression.setImageBitmap(thumbBitmap);

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });*/
    }

    //------------------------------------------------------------------------------------------
    protected void OPEN_DEVICE() {
        String msg;

        m_hUSB = m_cHost.OpenDeviceInterfaces();

        if (m_hUSB < 0) {
            msg = "Can't open host usb !";
            m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
            displayMessageOnUIThread("Opened Device Status", msg);
            return;
        }

        m_hDevice = m_cLAPI.OpenDevice(m_hUSB);

        if (m_hDevice == 0) msg = "Can't open device !";
        else {
            msg = "OpenDevice() = OK";
//            EnableBittons (BUTTONS_INITIALIZED);
        }

        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
    }

    //------------------------------------------------------------------------------------------
    protected void CLOSE_DEVICE() {
        int ret;
        String msg;
        ret = m_cLAPI.CloseDevice(m_hDevice);
        if (ret != LAPI.TRUE) msg = "Can't close device !";
        else {
            m_cHost.CloseDeviceInterface();
            msg = "CloseDevice() = OK";
//            EnableBittons (BUTTONS_FINALIZED);
        }
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
    }

    //------------------------------------------------------------------------------------------
    protected void GET_IMAGE() {
        int ret;
        String msg;
        ret = m_cLAPI.GetImage(m_hDevice, m_image);
        if (ret != LAPI.TRUE) msg = "Can't get image !";
        else msg = "GetImage() = OK";
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE, LAPI.WIDTH, LAPI.HEIGHT, m_image));
    }

    //------------------------------------------------------------------------------------------
    protected void GET_IMAGE_QUALITY() {
        int qr;
        String msg;
        qr = m_cLAPI.GetImageQuality(m_image);
        msg = String.format("GetImageQuality() = %d", qr);
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
    }

    //------------------------------------------------------------------------------------------
    protected void CREATE_ANSI_TEMP() {
        int i, ret;
        String msg;
        ret = m_cLAPI.IsPressFinger(m_image);
        if (ret == 0) {
            msg = "IsPressFinger() = 0";
            m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
            return;
        }
        ret = m_cLAPI.CreateANSITemplate(m_image, m_itemplate_1);
        if (ret == 0) msg = "Can't create ANSI template !";
        else msg = "CreateANSITemplate() = OK";
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
        msg = "";
        for (i = 0; i < LAPI.FPINFO_STD_MAX_SIZE; i++) {
            msg += String.format("%02x", m_itemplate_1[i]);
        }
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_VIEW_TEMPLATE_1, 0, 0, msg));
    }

    //------------------------------------------------------------------------------------------
    protected void CREATE_ISO_TEMP() {
        int i, ret;
        String msg;
        ret = m_cLAPI.CreateISOTemplate(m_image, m_itemplate_2);
        if (ret == 0) msg = "Can't create ISO template !";
        else msg = "CreateISOTemplate() = OK";
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
        msg = "";
        for (i = 0; i < LAPI.FPINFO_STD_MAX_SIZE; i++) {
            msg += String.format("%02x", m_itemplate_2[i]);
        }
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_VIEW_TEMPLATE_2, 0, 0, msg));
    }

    //------------------------------------------------------------------------------------------
    protected void COMPARE_TEMPS() {
        int score;
        String msg;
        SaveAsTemplate("m_itemplate_1", m_itemplate_1);
        SaveAsTemplate("m_itemplate_2", m_itemplate_2);
        score = m_cLAPI.CompareTemplates(m_itemplate_1, m_itemplate_2);
        msg = String.format("CompareTemplates() = %d", score);
        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
    }

    //------------------------------------------------------------------------------------------
    public void SaveAsTemplate(String filename, byte[] itemplate) {
        File extStorageDirectory = Environment.getExternalStorageDirectory();
        File Dir = new File(extStorageDirectory, "Android");
        File file = new File(Dir, filename);
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(itemplate);
            out.close();
        } catch (Exception e) {
        }
    }

    public void closeVoltage() {
        FileWriter closefr;
        try {
            closefr = new FileWriter(mFile);
            closefr.write("1");
            closefr.close();
        } catch (Exception e) {
            Log.e("hm71", "wuyb--close---write error");
        }
    }

    private void displayMessageOnUIThread(final String title, final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageBox.Show(BiometricVerficationActivity.this, title, message);
            }
        });

    }

    // Download JSON file AsyncTask
    private class LOVManager extends AsyncTask<Void, Void, List<LOV>> {

        private LOVModel lovModel;
//        private Gson gsonResponse;

        public LOVManager(LOVModel lovModel) {
            this.lovModel = lovModel;
        }

        @Override
        protected List<LOV> doInBackground(Void... params) {

            List<LOV> LOVlist = new ArrayList<LOV>();
            String serverURL = AppConstants.BIOMETRIC_WS_URL;
            serverURL += "/" + lovModel.LOVName;
            try {

                String purposeJson = new RestFullClient().Get(serverURL);

                Log.d(TAG, "purposeJson : " + purposeJson);

                if (purposeJson != null) {

                    ArrayList<LOV> lovArray = new ArrayList<>();

                    String finalJsonResponse = "{\"data\": " + purposeJson + "}";

                    JSONObject jsonObject = new JSONObject(finalJsonResponse);
                    JSONArray valuesArray = jsonObject.getJSONArray("data");

                    Log.v(TAG, "valuesArray Length = " + valuesArray.length());
                    Log.v(TAG, "valuesArray = " + valuesArray);

                    for (int i = 0; i < valuesArray.length(); i++) {
                        JSONObject actor = valuesArray.getJSONObject(i);
                        String lovRow = actor.getString("LOVRow");
                        String lovValue = actor.getString("LOVValue");

                        LOV lovModel = new LOV();
                        lovModel.LOVRow = lovRow;
                        lovModel.LOVValue = lovValue;
                        lovArray.add(lovModel);
                    }

//                    GsonBuilder gsonBuilder = new GsonBuilder();
//                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
//                    gsonResponse = gsonBuilder.create();
//                    return Arrays.asList(gsonResponse.fromJson(finalJsonResponse, LOVModel[].class));

                    return lovArray;
                }

            } catch (IOException e) {
                // have centralize error handling
                Log.e(TAG, "Error occured while parsing JSON !, Error = " + e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "Error occured while parsing JSON !, Error = " + e.toString());
                e.printStackTrace();
            }

            return LOVlist;
        }

        protected void onPostExecute(List<LOV> response) {

            // Locate the spinner in activity_main.xml
            if (lovModel != null) {

                Spinner lovSpinner = (Spinner) findViewById(lovModel.SpinnerId);

                if (lovSpinner != null) {
                    lovSpinner.setAdapter(new ArrayAdapter<LOV>(BiometricVerficationActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            response));
                }

                loginProgressDialog.dismiss();
            }
        }
    }

}