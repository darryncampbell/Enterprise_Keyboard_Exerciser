package com.darryncampbell.ekbexerciser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DataWedgeUtilities {

    //  DataWedge API
    public static final String ACTION_DATAWEDGE_FROM_6_2 = "com.symbol.datawedge.api.ACTION";
    private static final String EXTRA_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";
    private static final String EXTRA_CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE";

    //  DataWedge Profile properties
    final static String PROFILE_NAME = "EKB_Exerciser";
    final static String PACKAGE_NAME = "com.darryncampbell.ekbexerciser";
    final static String ACTIVITY_NAME = ".SecondActivity";


    public static void CreateProfile(Context context)
    {
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE_FROM_6_2, EXTRA_CREATE_PROFILE, PROFILE_NAME, context);
    }

    public static void SetProfileConfig(String layoutGroupName, String layoutName, Context context) {
        Bundle bMain = new Bundle();
        Bundle bConfigEKB = new Bundle();

        Bundle appConfig = new Bundle();
        appConfig.putString("PACKAGE_NAME", PACKAGE_NAME);
        appConfig.putStringArray("ACTIVITY_LIST", new String[]{PACKAGE_NAME + ACTIVITY_NAME});
        bMain.putParcelableArray("APP_LIST", new Bundle[]{appConfig});

        Bundle bParamsEKB = new Bundle();
        bParamsEKB.putString("ekb_enabled", "true");
        Bundle layoutParams = new Bundle();
        layoutParams.putString("layout_group", layoutGroupName);
        layoutParams.putString("layout_name", layoutName);
        bParamsEKB.putBundle("ekb_layout", layoutParams);

        bConfigEKB.putString("RESET_CONFIG", "false");
        bConfigEKB.putBundle("PARAM_LIST", bParamsEKB);

        bMain.putBundle("EKB", bConfigEKB);

        bMain.putString("PROFILE_NAME", PROFILE_NAME);
        bMain.putString("PROFILE_ENABLED", "true");
        bMain.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");

        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE_FROM_6_2, EXTRA_SET_CONFIG, bMain, context);
    }

    private static void sendDataWedgeIntentWithExtra(String action, String extraKey, String extraValue, Context context)
    {
        Intent dwIntent = new Intent();
        dwIntent.setAction(action);
        dwIntent.putExtra(extraKey, extraValue);
        dwIntent.putExtra("SEND_RESULT", "FALSE");
        context.sendBroadcast(dwIntent);
    }

    private static void sendDataWedgeIntentWithExtra(String action, String extraKey, Bundle extraValue, Context context)
    {
        Intent dwIntent = new Intent();
        dwIntent.setAction(action);
        dwIntent.putExtra(extraKey, extraValue);
        dwIntent.putExtra("SEND_RESULT", "FALSE");
        context.sendBroadcast(dwIntent);
    }


}
