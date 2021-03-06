package com.darryncampbell.ekbexerciser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //  todo DataWedge logic in the other activity

    //  REPLACE THESE WITH YOUR CUSTOM LAYOUT GROUP NAME AND LAYOUT NAMES
    static final String LAYOUT_GROUP_NAME = "Test001";
    final String LAYOUT_NAME_ONE = "Layout001";
    final String LAYOUT_NAME_TWO = "Layout002";
    final String LAYOUT_NAME_THREE = "Layout003";
    static final String LAYOUT_DATAWEDGE_001 = "LayoutDW001";
    static final String LAYOUT_DATAWEDGE_002 = "LayoutDW002";

    final String EKB_ACTION_UPDATE = "com.symbol.ekb.api.ACTION_UPDATE";
    final String EKB_ACTION_GET = "com.symbol.ekb.api.ACTION_GET";
    final String EKB_ACTION_DO = "com.symbol.ekb.api.ACTION_DO";
    final String EKB_PACKAGE = "com.symbol.mxmf.csp.enterprisekeyboard";
    final String LOG_TAG = "EKB_Exerciser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSetKeyLayout1 = findViewById(R.id.btnSetKeyboardLayout1);
        btnSetKeyLayout1.setOnClickListener(this);
        Button btnEnable = findViewById(R.id.btnSetLayoutDW001);
        btnEnable.setOnClickListener(this);
        Button btnDisable = findViewById(R.id.btnSetLayoutDW002);
        btnDisable.setOnClickListener(this);
        Button btnSetKeyboardLayoutOne = findViewById(R.id.btnSetKeyboardLayout1);
        btnSetKeyboardLayoutOne.setOnClickListener(this);
        Button btnSetKeyboardLayoutTwo = findViewById(R.id.btnSetKeyboardLayout2);
        btnSetKeyboardLayoutTwo.setOnClickListener(this);
        Button btnSetKeyboardLayoutThree = findViewById(R.id.btnSetKeyboardLayout3);
        btnSetKeyboardLayoutThree.setOnClickListener(this);
        Button btnShowKeyboard = findViewById(R.id.btnShowKeyboard);
        btnShowKeyboard.setOnClickListener(this);
        Button btnHideKeyboard = findViewById(R.id.btnHideKeyboard);
        btnHideKeyboard.setOnClickListener(this);
        Button btnResetKeyboard = findViewById(R.id.btnResetKeyboardTrue);
        btnResetKeyboard.setOnClickListener(this);
        Button btnEKBViaDWDemo = findViewById(R.id.btnEKBViaDataWedgeDemo);
        btnEKBViaDWDemo.setOnClickListener(this);

        RequestAvailableLayouts();

        CreateDataWedgeProfile();
    }

    private void RequestCurrentLayout()
    {
        //  Retrieve the current keyboard layout
        Intent intent = new Intent();
        intent.setAction(EKB_ACTION_GET);
        intent.setPackage(EKB_PACKAGE);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        String[] propertiesToRetrieve = {"CURRENT_LAYOUT_GROUP", "CURRENT_LAYOUT_NAME"};
        intent.putExtra("PROPERTIES_TO_GET", propertiesToRetrieve);

        PendingIntent piResponse = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        intent.putExtra("CALLBACK_RESPONSE", piResponse);
        sendBroadcast(intent);
    }

    private void RequestAvailableLayouts()
    {
        Intent intent = new Intent();
        intent.setAction(EKB_ACTION_GET);
        intent.setPackage(EKB_PACKAGE);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        String[] propertiesToRetrieve = {"AVAILABLE_LAYOUTS"};
        intent.putExtra("PROPERTIES_TO_GET", propertiesToRetrieve);
        PendingIntent piResponse = PendingIntent.getActivity(getApplicationContext(), 1,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        intent.putExtra("CALLBACK_RESPONSE", piResponse);
        sendBroadcast(intent);
    }

    private void ProcessEKBResponse(Intent intent) {
        final Bundle mBundle = intent.getExtras();
        String result = mBundle.getString("RESULT_CODE");
        String msg = mBundle.getString("RESULT_MESSAGE");
        Log.i(LOG_TAG, "From EKB: (result=" + result + ", msg=" + msg + ")");
        String currLayoutGroup = "unknown";
        String currLayoutName = "unknown";

        //  Handle current layout name
        if(mBundle.get("CURRENT_LAYOUT_GROUP") != null) {
            currLayoutGroup = (String) mBundle.get("CURRENT_LAYOUT_GROUP");
            if(mBundle.get("CURRENT_LAYOUT_NAME") != null) {
                currLayoutName = (String) mBundle.get("CURRENT_LAYOUT_NAME");
            }
            TextView currentLayout = findViewById(R.id.txtCurrentLayout);
            currentLayout.setText("Group: " + currLayoutGroup + ", Name: " + currLayoutName);
        }

        //  Handle available layouts
        if(mBundle.get("AVAILABLE_LAYOUTS") != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Object[] respObj = (Object[]) mBundle.getParcelableArray("AVAILABLE_LAYOUTS");
                    for(int i = 0; i < respObj.length; i++) {
                        Bundle temp = (Bundle) respObj[i];
                        String customLayouts = "";
                        customLayouts += "Group: ";
                        customLayouts += temp.getString("LAYOUT_GROUP");
                        customLayouts += " (";
                        Object[] layoutNamesBundle = (Object[]) temp.get("LAYOUTS");
                        for(int j = 0; j <layoutNamesBundle.length; j++) {
                            Bundle tempBundle = (Bundle) layoutNamesBundle[j];
                            customLayouts += "Layout:";
                            customLayouts += tempBundle.getString("LAYOUT_NAME");
                            customLayouts += " ";
                        }
                        customLayouts += ") ";
                        TextView availableLayouts = findViewById(R.id.txtAvailableLayouts);
                        availableLayouts.setText(customLayouts);
                    }
                }
            });
            RequestCurrentLayout();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        ProcessEKBResponse(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetLayoutDW001:
                EnableKeyboard(true);
                break;
            case R.id.btnSetLayoutDW002:
                EnableKeyboard(false);
                break;
            case R.id.btnSetKeyboardLayout1:
                SetKeyboardLayout(LAYOUT_GROUP_NAME, LAYOUT_NAME_ONE);
                RequestAvailableLayouts();
                break;
            case R.id.btnSetKeyboardLayout2:
                SetKeyboardLayout(LAYOUT_GROUP_NAME, LAYOUT_NAME_TWO);
                RequestAvailableLayouts();
                break;
            case R.id.btnSetKeyboardLayout3:
                SetKeyboardLayout(LAYOUT_GROUP_NAME, LAYOUT_NAME_THREE);
                RequestAvailableLayouts();
                break;
            case R.id.btnShowKeyboard:
                ShowKeyboard(true);
                break;
            case R.id.btnHideKeyboard:
                ShowKeyboard(false);
                break;
            case R.id.btnResetKeyboardTrue:
                ResetKeyboard(true);
                break;
            case R.id.btnEKBViaDataWedgeDemo:
                startActivity(new Intent(this, SecondActivity.class));
                return;
        }
        RequestCurrentLayout();
    }

    private void ResetKeyboard(boolean bReset) {
        Intent intent = new Intent();
        intent.setAction(EKB_ACTION_DO);
        intent.setPackage(EKB_PACKAGE);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("RESET_LAYOUT", bReset);
        PendingIntent piResponse = PendingIntent.getActivity(getApplicationContext(), 5,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        intent.putExtra("CALLBACK_RESPONSE", piResponse);
        sendBroadcast(intent);
    }

    private void EnableKeyboard(boolean bEnable) {
        Intent intent = new Intent();
        intent.setAction(EKB_ACTION_UPDATE);
        intent.setPackage(EKB_PACKAGE);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("ENABLE", bEnable);
        PendingIntent piResponse = PendingIntent.getActivity(getApplicationContext(), 4,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        intent.putExtra("CALLBACK_RESPONSE", piResponse);
        sendBroadcast(intent);
    }

    private void ShowKeyboard(boolean bShowKeyboard) {
        Intent intent = new Intent();
        intent.setAction(EKB_ACTION_UPDATE);
        intent.setPackage(EKB_PACKAGE);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("SHOW", bShowKeyboard);
        PendingIntent piResponse = PendingIntent.getActivity(getApplicationContext(), 2,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        intent.putExtra("CALLBACK_RESPONSE", piResponse);
        sendBroadcast(intent);
    }

    private void SetKeyboardLayout(String group_name, String layout_name) {
        ShowKeyboard(false);
        Intent intent = new Intent();
        intent.setAction(EKB_ACTION_UPDATE);
        intent.setPackage(EKB_PACKAGE);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("CURRENT_LAYOUT_GROUP", group_name);
        intent.putExtra("CURRENT_LAYOUT_NAME", layout_name);
        PendingIntent piResponse = PendingIntent.getActivity(getApplicationContext(), 3,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        intent.putExtra("CALLBACK_RESPONSE", piResponse);
        sendBroadcast(intent);
        ShowKeyboard(true);
    }

    private void CreateDataWedgeProfile() {
        DataWedgeUtilities.CreateProfile(getApplicationContext());
        DataWedgeUtilities.SetProfileConfig(LAYOUT_GROUP_NAME, LAYOUT_DATAWEDGE_001, getApplicationContext());
    }
}
