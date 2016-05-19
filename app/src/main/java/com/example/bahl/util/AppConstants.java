package com.example.bahl.util;

/**
 * Created by Administrator on 5/13/2016.
 */
public class AppConstants {
    public static final String KEY_JSON_RESPONSE = "jsonResponse";
    public static final String KEY_DOB = "DateofBirth";
    public static final String KEY_CNIC_IMG = "CustomerImage";
    public static final String KEY_CNIC_EXPIRY = "CardExpiry";
    public static final String KEY_CNIC_ADDRESS = "Address";
    public static final String KEY_CNIC_NAME = "CNICName";
    public static final String KEY_CNIC_NO = "CNIC";
    public static final String KEY_CNIC_NADRA_MESSAGE = "C";
    public static final String KEY_USER_SESSION_PREFERENCE = "User_Session_Preference";
    public static final String KEY_USER_SESSION = "User_Session";



    /// URL
    public static final String WEBSERVICE_URL = "http://10.130.15.145/OrbitWebApi";
    public static final String AUTHENTICATION_WS_URL = WEBSERVICE_URL + "/Security";
    public static final String AUTHENTICATION_WS_LOGIN_URL = AUTHENTICATION_WS_URL +"/Login";
    public static final String AUTHENTICATION_WS_LOGOUT_URL = AUTHENTICATION_WS_URL +"/Logout";
    public static final String BIOMETRIC_WS_URL = WEBSERVICE_URL + "/Biometric";
    public static final String BIOMETRIC_Verify_WS_URL = BIOMETRIC_WS_URL + "/Verification";



}
