package com.example.rcpc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class settingsActivity extends AppCompatActivity {

    Spinner spnProcesses;
    EditText txtIp, txtSubnet;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtIp = findViewById(R.id.editTextIP);
        txtSubnet = findViewById(R.id.editTextSubnet);
        confirm = findViewById(R.id.btnConfirm);
        spnProcesses = findViewById(R.id.spinProcesses);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString(MainActivity.IP, txtIp.getText().toString());
                editor.putString(MainActivity.Subnet, txtSubnet.getText().toString());

                editor.commit();

                startActivity(new Intent(settingsActivity.this, MainActivity.class));
            }
        });

    }
}
