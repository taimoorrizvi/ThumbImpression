package com.IDWORLD;

import android.util.Log;

public class LAPI {
    //****************************************************************************************************
    public static final int FPINFO_STD_MAX_SIZE = 1024;//512
    public static final int DEF_QUALITY_SCORE = 30;
    public static final int DEF_MATCH_SCORE = 45;
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    static final String TAG = "LAPI";
    //****************************************************************************************************
    public static int WIDTH = 208;
    public static int HEIGHT = 288;
    public static int IMAGE_SIZE = WIDTH * HEIGHT;

    //****************************************************************************************************
    static {
        try {
            System.loadLibrary("biofp_e_lapi");
        } catch (UnsatisfiedLinkError e) {
            Log.e("LAPI", "biofp_e_lapi", e);
        }
    }

    public LAPI() {
    }

    public int GetWidth() {
        return WIDTH;
    }

    public int GetHeight() {
        return HEIGHT;
    }

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function returns string version of the Finger Recognition SDK.
    // Function  : GetVersion
    // Arguments : void
    // Return    : String :
    //				   return string version of the Finger Library SDK.
    //------------------------------------------------------------------------------------------------//
    public native String GetVersion();

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function finalizes the Finger Recognition SDK Library.
    // Function  : OpenDevice
    // Arguments :
    // Return    : int :
    //				   If successful, return handle of devide, else 0.
    //------------------------------------------------------------------------------------------------//
    public native long OpenDevice(int fd);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function finalizes the Finger Recognition SDK Library.
    // Function  : CloseDevice
    // Arguments :
    // Return    : int :
    //				   If successful, return 1, else 0.
    //------------------------------------------------------------------------------------------------//
    public native int CloseDevice(long device);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function returns UUID of this device.
    // Function  : GetRegisterID
    // Arguments :
    //             : used with the return of function "OpenDevice()"
    // Return    : String :
    //				   return registration id for license agreement.
    //------------------------------------------------------------------------------------------------//
    public native String GetUUID(long device);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function return image captured from this device.
    // Function  : GetImage
    // Arguments : void
    //        device : used with the return of function "OpenDevice()"
    // Return    : byte[]
    //				   If successful, return 1, else 0
    //------------------------------------------------------------------------------------------------//
    public native int GetImage(long device, byte[] image);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function lets calibration of this sensor device.
    // Function  : Calibration
    // Arguments : void
    //         device : used with the return of function "OpenDevice()"
    // Return    : byte[]
    //				   If successful, return 1, else 0
    //------------------------------------------------------------------------------------------------//
    public native int Calibration(long device);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function checks whether finger is on sensor of this device or not.
    // Function  : IsPressFinger
    // Arguments : void
    //		(In) : byte[] image : image return by function "GetImage()"
    // Return    : int
    //				   return percent value measured finger-print on sensor(0~100).
    //------------------------------------------------------------------------------------------------//
    public native int IsPressFinger(byte[] image);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function creates the ANSI standard template from the uncompressed raw image.
    // Function  : CreateStdTemplate
    // Arguments : void
    //		(In) : byte[] image : image return by function "GetImage()"
    //	(In/Out) : byte[] itemplate : template created from image.
    // Return    : int :
    //				   If this function successes, return none-zero, else 0.
    //------------------------------------------------------------------------------------------------//
    public native int CreateANSITemplate(byte[] image, byte[] itemplate);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function creates the ISO standard template from the uncompressed raw image.
    // Function  : CreateStdTemplate
    // Arguments : void
    //		(In) : byte[] image : image return by function "GetImage()"
    //	(In/Out) : byte[] itemplate : template created from image.
    // Return    : int :
    //				   If this function successes, return none-zero, else 0.
    //------------------------------------------------------------------------------------------------//
    public native int CreateISOTemplate(byte[] image, byte[] itemplate);

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function gets the quality value of fingerprint raw image.
    // Function  : GetImageQuality
    // Arguments : void
    //		(In) : byte[] image : image return by function "GetImage()"
    // Return    : int :
    //				   If this function successes, return quality value(0~100), else 0.
    //------------------------------------------------------------------------------------------------//
    public native int GetImageQuality(byte[] image);
    //****************************************************************************************************

    //------------------------------------------------------------------------------------------------//
    // Purpose   : This function matches two templates and return similar match score.
    //             This function is for 1:1 Matching and only used in finger-print verification.
    // Function  : CompareTemplates
    // Arguments :
    //			(In) : byte[] itemplateToMatch : template to match :
    //                 This template must be used as that is created by function "CreateANSITemplate()".
    //                 or function "CreateISOTemplate()".
    //			(In) : byte[] itemplateToMatched : template to be matched
    //                 This template must be used as that is created by function "CreateANSITemplate()".
    //                 or function "CreateISOTemplate()".
    // Return    : int : return similar match score(0~100) of two finger-print templates.
    //------------------------------------------------------------------------------------------------//
    public native int CompareTemplates(byte[] itemplateToMatch, byte[] itemplateToMatched);
}
