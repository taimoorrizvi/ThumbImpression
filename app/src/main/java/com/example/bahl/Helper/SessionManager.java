package com.example.bahl.Helper;

import android.content.SharedPreferences;
import android.view.View;

import com.example.bahl.util.AppConstants;

/**
 * Created by Administrator on 5/16/2016.
 */
public class SessionManager {

    public static boolean CreateSession(String token){

             /*  SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.KEY_USER_SESSION_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(AppConstants.KEY_USER_SESSION, loginJson);
                editor.commit();*/


       /* SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(Name, n);
        editor.putString(Phone, ph);
        editor.putString(Email, e);
        editor.commit();*/

        return  true;
    }

}
