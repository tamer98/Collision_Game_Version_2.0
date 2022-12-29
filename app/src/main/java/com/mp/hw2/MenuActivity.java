package com.mp.hw2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

public class MenuActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 12;
    SwitchCompat slow, sensorMode;
    SPManager spManager;

    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        slow = findViewById(R.id.slow);
        sensorMode = findViewById(R.id.sensor);
        spManager = new SPManager(this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        sensorMode.setChecked(spManager.getB("sensor"));
        slow.setChecked(spManager.getB("slow"));

        if (sensorMode.isChecked()) {
            sensorMode.setText("Button Mode");
        } else {
            sensorMode.setText("Sensor Mode");
        }
        if (slow.isChecked()) {
            slow.setText("Slow");
        } else {
            slow.setText("Fast");
        }

        sensorMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spManager.putB("sensor", sensorMode.isChecked());
            }
        });

        sensorMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sensorMode.setText("Button Mode");
                } else {
                    sensorMode.setText("Sensor Mode");
                }
            }
        });
        slow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    slow.setText("Slow");
                } else {
                    slow.setText("Fast");
                }
            }
        });

        slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spManager.putB("slow", slow.isChecked());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(
                    MenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    MenuActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void highScore(View view) {
        startActivity(new Intent(this, HighScoreActivity.class));
    }

    public void goBack(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }
}