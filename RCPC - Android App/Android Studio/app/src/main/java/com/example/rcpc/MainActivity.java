package com.example.rcpc;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSettings, btnMute, btnUnmute, btnVolumeUp, btnVolumeDown, btnVolumeGet, btnVolumeSet;
    EditText txtValue;
    static TextView txt = null;

    //Session stuff
    public static final String MyPREFERENCES = "Connection_Settings" ;
    public static final String IP = "ipKey";
    public static final String Subnet = "subnetKey";
    public static final String Protocol = "http";
    SharedPreferences sharedpreferences;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set view pointers
        btnSettings = findViewById(R.id.btnSettings);
        btnMute = findViewById(R.id.btnMute);
        btnUnmute = findViewById(R.id.btnUnMute);
        btnVolumeUp = findViewById(R.id.btnVolumeUp);
        btnVolumeDown = findViewById(R.id.btnVolumeDown);
        btnVolumeSet = findViewById(R.id.btnVolumeSet);
        btnVolumeGet = findViewById(R.id.btnVolumeGet);
        txt = findViewById(R.id.text1);
        txtValue = findViewById(R.id.editTextValue);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        // check if the settings were already initialized, if not - redirect user to settings page.
        if (sharedpreferences.getString(IP,"no_ip") == "no_ip" || sharedpreferences.getString(Subnet,"no_subnet") == "no_subnet")
            btnSettings.callOnClick();

        //set onclicks
        btnSettings.setOnClickListener(this);
        btnMute.setOnClickListener(this);
        btnUnmute.setOnClickListener(this);
        btnVolumeGet.setOnClickListener(this);
        btnVolumeSet.setOnClickListener(this);
        btnVolumeUp.setOnClickListener(this);
        btnVolumeDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Map<String, String> args = new HashMap<String,String>();
        args.put("value", txtValue.getText().toString());
        switch (v.getId())
        {
            case (R.id.btnSettings):
                startActivity(new Intent(MainActivity.this, settingsActivity.class));
                break;
            case (R.id.btnMute):
                args.put("code", Requests.VOLUME_MUTE.getValue());
                break;
            case (R.id.btnUnMute):
                args.put("code", Requests.VOLUME_UNMUTE.getValue());
                break;
            case (R.id.btnVolumeUp)  :
                args.put("code", Requests.VOLUME_UP.getValue());
                break;
            case (R.id.btnVolumeDown)  :
                args.put("code", Requests.VOLUME_DOWN.getValue());
                break;
            case (R.id.btnVolumeGet)  :
                args.put("code", Requests.VOLUME_GET.getValue());
                break;
            case (R.id.btnVolumeSet)  :
                args.put("code", Requests.VOLUME_SET.getValue());
                break;
            default:
                args.put("code", Requests.NONE.getValue());
                break;
        }
        //Send the request
        HTTPHelper.getInstance().SendRequest(getApplicationContext(),args,sharedpreferences);
    }
//    private String getDeviceIPAddress()
//    {
//        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//    }
}
