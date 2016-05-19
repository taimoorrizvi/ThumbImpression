///*package com.sample;
//
////------------------------------------------------------------------------------------------
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Base64;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.IDWORLD.HostUsb;
//import com.IDWORLD.LAPI;
//import com.crashlytics.android.Crashlytics;
//import com.example.bahl.Helper.MessageBox;
//import com.example.bahl.Helper.RestFullClient;
//import com.example.bahl.Model.BiometricDetailDto;
//import com.example.bahl.Model.BiometricVerificationDto;
//import com.example.bahl.Model.LOV;
//import com.example.bahl.Model.LOVModel;
//import com.example.bahl.bahl.BiometricDetailActivity;
//import com.example.bahl.bahl.LoginActivity;
//import com.example.bahl.util.AppConstants;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.synapse.thumbimpression.R;
//import com.synapse.thumbimpression.rest.ThumbVerificationTask;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import io.fabric.sdk.android.Fabric;
//
////------------------------------------------------------------------------------------------
////------------------------------------------------------------------------------------------
//
//public class FpSDKSampleP41MActivity extends AppCompatActivity {
//    //------------------------------------------------------------------------------------------
//    public static final int MSG_SHOW_TEXT = 101;
//    public static final int MSG_SHOW_IMAGE = 102;
//    public static final int MSG_VIEW_TEMPLATE_1 = 103;
//    public static final int MSG_VIEW_TEMPLATE_2 = 104;
//    //------------------------------------------------------------------------------------------
//    public static final int BUTTONS_INITIALIZED = 0;
//    public static final int BUTTONS_FINALIZED = 1;
//    public static final int BUTTONS_CAPTURERIZED = 2;
//    //------------------------------------------------------------------------------------------
//    public static final int MESSAGE_ID_ENABLED = 403;
//    public static final int BUTTONS_ID_ENABLED_FINALIZED = 404;
//    public static final int BUTTONS_ID_ENABLED_INITIALIZED = 405;
//    public static final int BUTTONS_ID_ENABLED_CAPTURERIZED = 406;
//    final File mFile = new File("/sys/class/power_supply/usb/device/CONTROL_GPIO114");
//    private final String TAG = FpSDKSampleP41MActivity.class.getSimpleName();
//    String imagebytestring;
//    //------------------------------------------------------------------------------------------
//    private Button btnOpen;
//    private Button btnClose;
//    private Button btnGetImage;
//    private Button btnVerifyThumb;
//    private Button btnGetImageQuality;
//    private Button btnCreateAnsiTemp;
//    private Button btnCreateIsoTemp;
//    private Button btnMatchTemp;
//    private TextView tvMessage;
//    private TextView tvTemp1;
//    private TextView tvTemp2;
//    private TextView productCategoryTV;
//    private ImageView viewFinger;
//    //------------------------------------------------------------------------------------------
//    private HostUsb m_cHost;
//    private LAPI m_cLAPI;
//    private int m_hUSB = 0;
//    private long m_hDevice = 0;
//    //------------------------------------------------------------------------------------------
//    private byte[] m_image = new byte[LAPI.WIDTH * LAPI.HEIGHT];
//    private byte[] m_itemplate_1 = new byte[LAPI.FPINFO_STD_MAX_SIZE];
//    private byte[] m_itemplate_2 = new byte[LAPI.FPINFO_STD_MAX_SIZE];
//    private ThumbVerificationTask thumbVerificationTask;
//    private ImageView ivthumbImpression;
//    private ProgressDialog lovProgressDialog;
//    private ProgressDialog saveProgressDialog;
//    private Button btnSave;
//    private LOV selectedCustomerCategory;
//
//
//    private final Handler m_fEvent = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_SHOW_TEXT:
//                    tvMessage.setText((String)msg.obj);
//                    break;
//                case MSG_VIEW_TEMPLATE_1:
//                    tvTemp1.setText((String)msg.obj);
//                    break;
//                case MSG_VIEW_TEMPLATE_2:
//                    tvTemp2.setText((String)msg.obj);
//                    break;
//                case MESSAGE_ID_ENABLED:
//                    Button btn = (Button) findViewById(msg.arg1);
//                    if (msg.arg2 != 0) btn.setEnabled(true);
//                    else btn.setEnabled(false);
//                    break;
//                case BUTTONS_ID_ENABLED_INITIALIZED:
//                    EnableBittons(BUTTONS_INITIALIZED);
//                    break;
//                case BUTTONS_ID_ENABLED_FINALIZED:
//                    EnableBittons(BUTTONS_FINALIZED);
//                    break;
//                case BUTTONS_ID_ENABLED_CAPTURERIZED:
//                    EnableBittons(BUTTONS_CAPTURERIZED);
//                    break;
//                case MSG_SHOW_IMAGE:
//                    ShowFingerBitmap ((byte[])msg.obj,msg.arg1,msg.arg2);
//                    break;
//            }
//        }
//    };
//
//
//    /*private final Handler m_fEvent = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_SHOW_TEXT:
//                    tvMessage.setText((String) msg.obj);
////                    String ansiDataString = (String) msg.obj;
////                    MessageBox.Show(FpSDKSampleP41MActivity.this, "Thumb ANSI Data","" +ansiDataString);
////                    sendDataToEndPoint(ansiDataString);
//
////                    productCategoryTV.setText((String) msg.obj);
//
//
//                    break;
//                case MSG_VIEW_TEMPLATE_1:
//                    tvTemp1.setText((String) msg.obj);
//
//                    String ansiDataString = (String) msg.obj;
//                    String tempString = ansiDataString;
//
//                    String finalEncodedString = "";
//                    try {
//                        finalEncodedString = Base64.encodeToString(tempString.getBytes("UTF-8"), Base64.DEFAULT);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    thumbImpressionANSIString = finalEncodedString;
//
//                    Log.d(TAG, "thumbImpressionANSIString = " + thumbImpressionANSIString);
//
//                    MessageBox.Show(FpSDKSampleP41MActivity.this, "tempString", "" + tempString);
//                    MessageBox.Show(FpSDKSampleP41MActivity.this, "thumbImpressionANSIString", "" + thumbImpressionANSIString);
//
////                    productCategoryTV.setText((String) msg.obj);
//                    break;
//                case MSG_VIEW_TEMPLATE_2:
//                    tvTemp2.setText((String) msg.obj);
////                    productCategoryTV.setText((String) msg.obj);
//                    break;
//                case MESSAGE_ID_ENABLED:
//                    Button btn = (Button) findViewById(msg.arg1);
//                    if (msg.arg2 != 0) btn.setEnabled(true);
//                    else btn.setEnabled(false);
//                    break;
//                case BUTTONS_ID_ENABLED_INITIALIZED:
//                    EnableBittons(BUTTONS_INITIALIZED);
//                    break;
//                case BUTTONS_ID_ENABLED_FINALIZED:
//                    EnableBittons(BUTTONS_FINALIZED);
//                    break;
//                case BUTTONS_ID_ENABLED_CAPTURERIZED:
//                    EnableBittons(BUTTONS_CAPTURERIZED);
//                    break;
//                case MSG_SHOW_IMAGE: {
//                    ShowFingerBitmap((byte[]) msg.obj, msg.arg1, msg.arg2);
//                    //Send the data for Verification here
//                }
//                break;
//            }
//        }
//    };*/
//
//    @Override
//    protected void onDestroy() {
//        CLOSE_DEVICE();
//        super.onDestroy();
//        closeVoltage();
//        if(saveProgressDialog!=null){
//            saveProgressDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//      /*  Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mActionBarToolbar.setTitle(R.string.action_logout);
//        setSupportActionBar(mActionBarToolbar);*/
//
//        btnOpen = (Button) findViewById(R.id.btnOpenDevice);
//        btnClose = (Button) findViewById(R.id.btnCloseDevice);
//        btnGetImage = (Button) findViewById(R.id.btnGetImage);
//        btnVerifyThumb = (Button) findViewById(R.id.btnVerifyThumb);
//        btnGetImageQuality = (Button) findViewById(R.id.btnGetImageQuality);
//        btnCreateAnsiTemp = (Button) findViewById(R.id.btnCreateANSITemp);
//        btnCreateIsoTemp = (Button) findViewById(R.id.btnCreateISOTemp);
//        btnMatchTemp = (Button) findViewById(R.id.btnMatchTemp);
//        tvMessage = (TextView) findViewById(R.id.tvMessage);
//        tvTemp1 = (TextView) findViewById(R.id.tvTemp1);
//        tvTemp2 = (TextView) findViewById(R.id.tvTemp2);
//        viewFinger = (ImageView) findViewById(R.id.ivImageViewer);
//
//        productCategoryTV = (TextView) findViewById(R.id.productCategoryTV);
//
//        EnableBittons(BUTTONS_FINALIZED);
//
//        lovProgressDialog = ProgressDialog.show(this, "Please wait ...", "Loading....", true);
//
//        Fabric.with(this, new Crashlytics());
//
//        // Save biometric data to server
//        btnSave = (Button) findViewById(R.id.btnSave);
//        if (btnSave != null) {
//            btnSave.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    saveProgressDialog = ProgressDialog.show(FpSDKSampleP41MActivity.this, "Saving ...", "Please wait ...", true);
//                    CREATE_ISO_TEMP();
//                    BiometricVerificationDto biometricVerificationDto = getBiometricVerification();
//                    if (biometricVerificationDto != null) {
//
//                        new SaveBiometricTask(biometricVerificationDto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    }
//
//                }
//            });
//        }
//
//
//
//        Spinner spinnerCustomerCategory = (Spinner) findViewById(R.id.spinnerCustomerCategory);
//        if (spinnerCustomerCategory != null) {
//            spinnerCustomerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                    selectedCustomerCategory = (LOV) parentView.getSelectedItem();
//                    String docTypeUrl = "IdentityDocType/" + selectedCustomerCategory.LOVRow;
//                    new LOVManager(new LOVModel(R.id.spinneridentityType, docTypeUrl)).execute();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parentView) {
//                    // your code here
//                }
//            });
//        }
//
//        // Load LOV
////         lovManager =  new LOVManager(new LOVModel(R.id.spinnerPurpose, "Purpose"));
////        lovManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        new LOVManager(new LOVModel(R.id.spinnerPurpose, "Purpose")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new LOVManager(new LOVModel(R.id.spinnerCustomerCategory, "CustomerCategory")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new LOVManager(new LOVModel(R.id.spinnerContactType, "BioContract")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new LOVManager(new LOVModel(R.id.spinnerProductCategory, "ProductCategory")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new LOVManager(new LOVModel(R.id.spinnerFingerIndex, "FingerList")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//
//        m_cHost = new HostUsb(this);
//        m_cLAPI = new LAPI();
//
//        btnOpen.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        OPEN_DEVICE();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        });
//        btnClose.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        CLOSE_DEVICE();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        });
//        btnGetImage.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        // GET_IMAGE_QUALITY();
//                        GET_IMAGE();
////                        CREATE_ANSI_TEMP();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        });
//        btnGetImageQuality.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        GET_IMAGE_QUALITY();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        });
//        btnCreateAnsiTemp.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        CREATE_ANSI_TEMP();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        });
//       /* btnVerifyThumb.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        CREATE_ANSI_TEMP();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        }); */
//        btnCreateIsoTemp.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        CREATE_ISO_TEMP();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        });
//        btnMatchTemp.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Runnable r = new Runnable() {
//                    public void run() {
//                        COMPARE_TEMPS();
//                    }
//                };
//                Thread s = new Thread(r);
//                s.start();
//            }
//        });
//
////		btnOpen.performClick();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_fpsdksamplep41m, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.action_logout:
//
//                Intent it = new Intent(FpSDKSampleP41MActivity.this, LoginActivity.class);
//
//                startActivity(it);
//
//
//                break;
//
//
//            default:
//                break;
//        }
//
//        return true;
//    }
//
//    public void openVoltage() {
//        try {
//            FileReader inCmd = new FileReader(mFile);
//            int i = inCmd.read();
//            inCmd.close();
//        } catch (Exception e) {
//            Log.e("hm71", "wuyb--open--write error");
//        }
//    }
//
//    public void closeVoltage() {
//        FileWriter closefr;
//        try {
//            closefr = new FileWriter(mFile);
//            closefr.write("1");
//            closefr.close();
//        } catch (Exception e) {
//            Log.e("hm71", "wuyb--close---write error");
//        }
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_HOME:
//                closeVoltage();
//                finish();
//                break;
//            case KeyEvent.KEYCODE_BACK:
//                closeVoltage();
//                finish();
//                break;
//            default:
//                break;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//
//        Intent getintent = getIntent();
//        boolean first = getintent.getBooleanExtra("first", false);
//
//        if (first) {
//            Log.e("hm71", "wuyb----first time do nothing");
//        } else {
//            openVoltage();
//            Log.e("hm71", "wuyb---openvoltage----not first time");
//        }
//
//        super.onResume();
//
////        Runnable r = new Runnable() {
////            public void run() {
////                OPEN_DEVICE();
////            }
////        };
////        Thread s = new Thread(r);
////        s.start();
//    }
//
//    //------------------------------------------------------------------------------------------
//    protected void OPEN_DEVICE() {
//        String msg;
//        m_hUSB = m_cHost.OpenDeviceInterfaces();
//        if (m_hUSB < 0) {
//            msg = "Can't open host usb !";
//            m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//            return;
//        }
//        m_hDevice = m_cLAPI.OpenDevice(m_hUSB);
//        if (m_hDevice == 0) msg = "Can't open device !";
//        else {
//            msg = "OpenDevice() = OK";
//            EnableBittons(BUTTONS_INITIALIZED);
//        }
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//    }
//
//    //------------------------------------------------------------------------------------------
//    protected void CLOSE_DEVICE() {
//        int ret;
//        String msg;
//        ret = m_cLAPI.CloseDevice(m_hDevice);
//        if (ret != LAPI.TRUE) msg = "Can't close device !";
//        else {
//            m_cHost.CloseDeviceInterface();
//            msg = "CloseDevice() = OK";
//            EnableBittons(BUTTONS_FINALIZED);
//        }
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//    }
//
//    //------------------------------------------------------------------------------------------
////    protected void GET_IMAGE() {
////        int ret;
////        String msg;
////        ret = m_cLAPI.GetImage(m_hDevice, m_image);
////        if (ret != LAPI.TRUE) msg = "Can't get image !";
////        else msg = "GetImage() = OK";
////        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
////        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE, LAPI.WIDTH, LAPI.HEIGHT, m_image));
////    }
//
//    //------------------------------------------------------------------------------------------
//    protected void GET_IMAGE()
//    {
//        int ret;
//        String msg;
//        ret = m_cLAPI.GetImage(m_hDevice, m_image);
//        if (ret != LAPI.TRUE) msg = "Can't get image !";
//        else msg = "GetImage() = OK";
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,msg));
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE, LAPI.WIDTH, LAPI.HEIGHT,m_image));
//    }
//
//    //------------------------------------------------------------------------------------------
//    protected void GET_IMAGE_QUALITY() {
//        int qr;
//        String msg;
//        qr = m_cLAPI.GetImageQuality(m_image);
//        msg = String.format("GetImageQuality() = %d", qr);
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//    }
//
//    //------------------------------------------------------------------------------------------
////    protected void CREATE_ANSI_TEMP() {
////        int i, ret;
////        String msg;
////        String rawmsg = "";
////        ret = m_cLAPI.IsPressFinger(m_image);
////        if (ret == 0) {
////            msg = "IsPressFinger() = 0";
////            m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
////            return;
////        }
////        ret = m_cLAPI.CreateANSITemplate(m_image, m_itemplate_1);
////
////        if (ret == 0) msg = "Can't create ANSI template !";
////        else msg = "CreateANSITemplate() = OK";
////        imagebytestring = msg;
////        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
////        msg = "";
////
////        for (i = 0; i < LAPI.FPINFO_STD_MAX_SIZE; i++) {
////            //  rawmsg += String.format("%02x", m_itemplate_1[i]);
////            msg += m_itemplate_1[i];
////        }
////        /*int msgSize = m_image.length;
////        for (i = 0; i < msgSize; i++) {
////            //  rawmsg += String.format("%02x", m_itemplate_1[i]);
////            rawmsg += m_image[i];
////        }*/
////        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_VIEW_TEMPLATE_1, 0, 0, msg + "||||" + rawmsg));
////    }
////------------------------------------------------------------------------------------------
//    protected void CREATE_ANSI_TEMP() {
//        int i, ret;
//        String msg;
//        ret = m_cLAPI.IsPressFinger(m_image);
//        if (ret == 0) {
//            msg = "IsPressFinger() = 0";
//            m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//            return;
//        }
//        ret = m_cLAPI.CreateANSITemplate(m_image, m_itemplate_1);
//        if (ret == 0) msg = "Can't create ANSI template !";
//        else msg = "CreateANSITemplate() = OK";
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//        msg = "";
//        for (i = 0; i < LAPI.FPINFO_STD_MAX_SIZE; i++) {
//            msg += String.format("%02x", m_itemplate_1[i]);
//        }
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_VIEW_TEMPLATE_1, 0, 0, msg));
//    }
//
//    //------------------------------------------------------------------------------------------
//    protected void CREATE_ISO_TEMP() {
//        int i, ret;
//        String msg;
//        ret = m_cLAPI.CreateISOTemplate(m_image, m_itemplate_2);
//        if (ret == 0) msg = "Can't create ISO template !";
//        else msg = "CreateISOTemplate() = OK";
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//        msg = "";
//
////        for (i = 0; i < LAPI.FPINFO_STD_MAX_SIZE; i++) {
////            msg += String.format("%02x", m_itemplate_2[i]);
////        }
////        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_VIEW_TEMPLATE_2, 0, 0, msg));
//    }
//
//    //------------------------------------------------------------------------------------------
//    protected void COMPARE_TEMPS() {
//        int score;
//        String msg;
//        SaveAsTemplate("m_itemplate_1", m_itemplate_1);
//        SaveAsTemplate("m_itemplate_2", m_itemplate_2);
//        score = m_cLAPI.CompareTemplates(m_itemplate_1, m_itemplate_2);
//        msg = String.format("CompareTemplates() = %d", score);
//        m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
//    }
//
//    //------------------------------------------------------------------------------------------
//    public void SaveAsTemplate(String filename, byte[] itemplate) {
//        File extStorageDirectory = Environment.getExternalStorageDirectory();
//        File Dir = new File(extStorageDirectory, "Android");
//        File file = new File(Dir, filename);
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            out.write(itemplate);
//            out.close();
//        } catch (Exception e) {
//        }
//    }
//
//    protected void EnableBittons(int state) {
//        switch (state) {
//            case BUTTONS_INITIALIZED://Open Button is only Enabled
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnOpenDevice, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCloseDevice, 1));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnGetImage, 1));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnGetImageQuality, 1));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCreateANSITemp, 1));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCreateISOTemp, 1));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnMatchTemp, 1));
//                break;
//            case BUTTONS_FINALIZED://Open Button is only disabled
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnOpenDevice, 1));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCloseDevice, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnGetImage, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnGetImageQuality, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCreateANSITemp, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCreateISOTemp, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnMatchTemp, 0));
//                break;
//            case BUTTONS_CAPTURERIZED://GetImage Button is only Enabled
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnOpenDevice, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCloseDevice, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnGetImage, 1));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnGetImageQuality, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCreateANSITemp, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnCreateISOTemp, 0));
//                m_fEvent.sendMessage(m_fEvent.obtainMessage(MESSAGE_ID_ENABLED, R.id.btnMatchTemp, 0));
//                break;
//        }
//    }
//
//    private void ShowFingerBitmap(byte[] image, int width, int height) {
//        if (width == 0) return;
//        if (height == 0) return;
//
//        int[] RGBbits = new int[width * height];
//        viewFinger.invalidate();
////		ivthumbImpression.invalidate();
//        for (int i = 0; i < width * height; i++) {
//            int v;
//            if (image != null) v = image[i] & 0xff;
//            else v = 0;
//            RGBbits[i] = Color.rgb(v, v, v);
//        }
//        Bitmap bmp = Bitmap.createBitmap(RGBbits, width, height, Config.RGB_565);
//
////		thumbVerificationTask = new ThumbVerificationTask(FpSDKSampleP41MActivity.this, bmp);
////		thumbVerificationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//        //Image to base 64 String code
////        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        // Must compress the Image to reduce image size to make upload easy
////        bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);
////        byte[] byte_arr = stream.toByteArray();
//        // Encode Image to String
////        String enchodedString = Base64.encodeToString(byte_arr, 0);
//
//        viewFinger.setImageBitmap(bmp);
////		ivthumbImpression.setImageBitmap(bmp);
//    }
//
//    private BiometricVerificationDto getBiometricVerification() {
//
//        try {
//            BiometricVerificationDto biometricVerificationDto = new BiometricVerificationDto();
//
//            EditText txtContactNo = (EditText) findViewById(R.id.txtContactNo);
//            EditText txtIdentityNo = (EditText) findViewById(R.id.txtIdentityNo);
//            Spinner spinnerProductCategory = (Spinner) findViewById(R.id.spinnerProductCategory);
//            Spinner spinnerFingerIndex = (Spinner) findViewById(R.id.spinnerFingerIndex);
//            Spinner spinnerPurpose = (Spinner) findViewById(R.id.spinnerPurpose);
//            Spinner spinnerContactType = (Spinner) findViewById(R.id.spinnerContactType);
//            Spinner spinneridentityType = (Spinner) findViewById(R.id.spinneridentityType);
//            short customerCategoryId = 0, productCategoryId = 0, purposeId = 0, contactTypeId = 0, identityTypeId = 0, fingerIndex = 0;
//            if (selectedCustomerCategory != null) {
//
//                biometricVerificationDto.CustomerCategory = selectedCustomerCategory.LOVValue;
//                customerCategoryId = Short.parseShort(selectedCustomerCategory.LOVRow);
//            }
//            if (spinnerProductCategory != null && spinnerProductCategory.getSelectedItem() != null) {
//                biometricVerificationDto.ProductCategory = ((LOV) spinnerProductCategory.getSelectedItem()).LOVValue;
//                productCategoryId = Short.parseShort(((LOV) spinnerProductCategory.getSelectedItem()).LOVRow);
//            }
//            if (spinnerPurpose != null && spinnerPurpose.getSelectedItem() != null) {
//
//                biometricVerificationDto.Purpose = ((LOV) spinnerPurpose.getSelectedItem()).LOVValue;
//                purposeId = Short.parseShort(((LOV) spinnerPurpose.getSelectedItem()).LOVRow);
//            }
//            if (spinnerContactType != null && spinnerContactType.getSelectedItem() != null) {
//
//                biometricVerificationDto.ContactType = ((LOV) spinnerContactType.getSelectedItem()).LOVValue;
//                contactTypeId = Short.parseShort(((LOV) spinnerContactType.getSelectedItem()).LOVRow);
//            }
//            if (spinneridentityType != null && spinneridentityType.getSelectedItem() != null) {
//
//                biometricVerificationDto.UserIdentificationDocument = ((LOV) spinneridentityType.getSelectedItem()).LOVValue;
//                identityTypeId = Short.parseShort(((LOV) spinneridentityType.getSelectedItem()).LOVRow);
//            }
//
//            if (spinnerFingerIndex != null && spinnerFingerIndex.getSelectedItem() != null) {
//
//                // biometricVerificationDto.BiometricFingerCode =((LOV)spinnerFingerIndex.getSelectedItem()).LOVValue;
//                fingerIndex = Short.parseShort(((LOV) spinnerFingerIndex.getSelectedItem()).LOVRow);
//            }
//
//            biometricVerificationDto.BiometricFingerCode = fingerIndex;
//            biometricVerificationDto.ContactTypeId = contactTypeId;
//            biometricVerificationDto.ContactNumber = txtContactNo != null ? txtContactNo.getText().toString() : "";
//            biometricVerificationDto.CustomerCategoryId = customerCategoryId;
//            // Akmal
//            //biometricVerificationDto.ImagebytesString = tvTemp1!=null?tvTemp1.getText().toString():"";// imagebytestring;;
//
//            //biometricVerificationDto.ImagebytesString = tvTemp2!=null?tvTemp2.getText().toString():"";// imagebytestring;;
//
//
//            biometricVerificationDto.ImagebytesString = Base64.encodeToString(m_itemplate_2, Base64.DEFAULT);
//
//            //biometricVerificationDto.Imagebytes = m_itemplate_2;
//
//            biometricVerificationDto.ProductCategoryId = productCategoryId;
//            biometricVerificationDto.PurposeId = purposeId;
//            biometricVerificationDto.UserIdentificationDocumentId = identityTypeId;
//            biometricVerificationDto.UserIdentificationDocumentNo = txtIdentityNo != null ? txtIdentityNo.getText().toString() : "";
//
//            return biometricVerificationDto;
//
//
//        } catch (Exception ex) {
//            Log.e(TAG, "Error occured while Connecting !, Error = " + ex.toString());
//            ex.printStackTrace();
//            return null;
//        }
//
//    }
//
//    // Download JSON file AsyncTask
//    private class LOVManager extends AsyncTask<Void, Void, List<LOV>> {
//
//        private LOVModel lovModel;
////        private Gson gsonResponse;
//
//        public LOVManager(LOVModel lovModel) {
//            this.lovModel = lovModel;
//        }
//
//        @Override
//        protected List<LOV> doInBackground(Void... params) {
//
//            List<LOV> LOVlist = new ArrayList<LOV>();
//            String serverURL = AppConstants.BIOMETRIC_WS_URL;// getString(R.string.webservice_Biometric_url);
//            serverURL += "/" + lovModel.LOVName;
//            try {
//
//                String purposeJson = new RestFullClient().Get(serverURL);
//
//                Log.d(TAG, "purposeJson : " + purposeJson);
//
//                if (purposeJson != null) {
//
//                    ArrayList<LOV> lovArray = new ArrayList<>();
//
//                    String finalJsonResponse = "{\"data\": " + purposeJson + "}";
//
//                    JSONObject jsonObject = new JSONObject(finalJsonResponse);
//                    JSONArray valuesArray = jsonObject.getJSONArray("data");
//
//                    Log.v(TAG, "valuesArray Length = " + valuesArray.length());
//                    Log.v(TAG, "valuesArray = " + valuesArray);
//
//                    for (int i = 0; i < valuesArray.length(); i++) {
//                        JSONObject actor = valuesArray.getJSONObject(i);
//                        String lovRow = actor.getString("LOVRow");
//                        String lovValue = actor.getString("LOVValue");
//
//                        LOV lovModel = new LOV();
//                        lovModel.LOVRow = lovRow;
//                        lovModel.LOVValue = lovValue;
//                        lovArray.add(lovModel);
//                    }
//
////                    GsonBuilder gsonBuilder = new GsonBuilder();
////                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
////                    gsonResponse = gsonBuilder.create();
////                    return Arrays.asList(gsonResponse.fromJson(finalJsonResponse, LOVModel[].class));
//
//                    return lovArray;
//                }
//
//            } catch (IOException e) {
//                // have centralize error handling
//                Log.e(TAG, "Error occured while parsing JSON !, Error = " + e.toString());
//                e.printStackTrace();
//            } catch (JSONException e) {
//                Log.e(TAG, "Error occured while parsing JSON !, Error = " + e.toString());
//                e.printStackTrace();
//            }
//
//            return LOVlist;
//        }
//
//        protected void onPostExecute(List<LOV> response) {
//
//            // Locate the spinner in activity_main.xml
//            if (lovModel != null) {
//
//                Spinner lovSpinner = (Spinner) findViewById(lovModel.SpinnerId);
//
//                if (lovSpinner != null) {
//                    lovSpinner.setAdapter(new ArrayAdapter<LOV>(FpSDKSampleP41MActivity.this,
//                            android.R.layout.simple_spinner_dropdown_item,
//                            response));
//                }
//
//                if (lovProgressDialog != null)
//                    lovProgressDialog.dismiss();
//            }
//        }
//    }
//
//    private class SaveBiometricTask extends AsyncTask<Void, Void, BiometricDetailDto> {
//
//        private BiometricVerificationDto biometricVerification;
//
//        public SaveBiometricTask(BiometricVerificationDto biometricVerification) {
//
//            this.biometricVerification = biometricVerification;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//
//        }
//
//        @Override
//        protected BiometricDetailDto doInBackground(Void... params) {
//
//            try {
//                String serverURL = AppConstants.BIOMETRIC_Verify_WS_URL;
//                //MessageBox.Show(getBaseContext(),"server", serverURL);
//                String biometricVerificationJson = new Gson().toJson(biometricVerification);
//                JSONObject biometricVerificationModelJson = new RestFullClient().Post(serverURL, biometricVerificationJson);
//                return new Gson().fromJson(biometricVerificationModelJson.toString(), BiometricDetailDto.class);
//
//            } catch (Exception e) {
//                Log.e(TAG, "Error occured while recieving Response Objecct!, Error = " + e.toString());
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(BiometricDetailDto responseObject) {
//            if (responseObject != null) {
//                if (biometricVerification.PurposeId == 2 && responseObject.IsValid) {
//                    MessageBox.Show(FpSDKSampleP41MActivity.this, "", getString(R.string.bank_internal_purpose_message));
//                } else if (responseObject.IsNadraError) {
//                    TextView tvNadraMessage = (TextView) findViewById(R.id.tvNadraMessage);
//                    if (tvNadraMessage != null) {
//                        tvNadraMessage.setText(responseObject.NadraMessage);
//                    }
//                } else {
//                    Intent it = new Intent(FpSDKSampleP41MActivity.this, BiometricDetailActivity.class);
//                    String biometricJson = new GsonBuilder().create().toJson(responseObject);
//                    it.putExtra(AppConstants.KEY_JSON_RESPONSE, "" + biometricJson);
//                    startActivity(it);
//                }
//            } else {
//
//                MessageBox.Show(FpSDKSampleP41MActivity.this, "Error", getString(R.string.server_side_error));
//            }
//            if (saveProgressDialog != null)
//                saveProgressDialog.dismiss();
//
//        }
//    }
//}