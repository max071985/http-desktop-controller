package com.example.rcpc;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

public class HTTPHelper {

    private final String default_ip = "0.0.0.0";
    private final String default_port = "8080";
    private final String default_code = "0x0";
    private final String default_value = "0";

    private static final HTTPHelper ourInstance = new HTTPHelper();

    public static HTTPHelper getInstance() {
        return ourInstance;
    }


    private HTTPHelper()
    {}

    public void SendRequest(Context context, String url)
    {
        try {

            final TextView txtResponseLabel = MainActivity.txt;

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    txtResponseLabel.setText(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    txtResponseLabel.setText(error.getMessage());
                }
            });

            requestQueue.add(stringRequest);
        }
        catch (Exception ex)
        {
            Log.d("ERROR:","Message: " + ex.getMessage());//debug
        }
    }

    public void SendRequest(Context context, Map<String,String> args, SharedPreferences preferences)
    {
        StringBuilder url = new StringBuilder();
        url.append(MainActivity.Protocol);
        url.append("://");
        url.append(preferences.getString(MainActivity.IP,default_ip));
        url.append("/?");

        for(String k : args.keySet())
        {
            String value = args.get(k);
            url.append(k + "=" + value);
            url.append("&");
        }
        url.deleteCharAt(url.length() - 1);

        Log.d("DEBUG:","Sending request to:" + url.toString()); //debug

        SendRequest(context,url.toString());
    }
}
