package com.example.rakesh.antiragging;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected Context context;
    protected double latitude,longitude;
    protected LocationManager locationManager;
    protected LocationListener listener;
    Button sendbtn;
    public static String messagestring ="";
    public static String s="";

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        sendbtn = (Button) findViewById(R.id.button1);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(s.toString(), messagestring.toString());
            }
        });
    }

        public void sendSMS(String phoneno, String sms) {
            SmsManager smsManager = SmsManager.getDefault();
            StringBuffer smsbody = new StringBuffer();
            smsbody.append(sms);
            smsbody.append("\n");
            smsbody.append("My location is-");
            smsbody.append("\n");
            smsbody.append("http://maps.google.com/maps?q=loc:");
            smsbody.append(String.valueOf(latitude));
            smsbody.append(",");
            smsbody.append(String.valueOf(longitude));
            smsbody.append("\n");
            smsManager.sendTextMessage(phoneno, null, smsbody.toString(), null, null);
            //Toast.makeText(this,smsbody,Toast.LENGTH_LONG).show();
            //Toast.makeText(this,sms.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1 || requestCode==2){
            Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item,menu);
        return super.onCreateOptionsMenu(menu);
    }

        public boolean onOptionsItemSelected(MenuItem item)
        {
            super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.about:
                startActivity(new Intent(this,About.class));
                break;
            case R.id.team:
                startActivity(new Intent(this,Team.class));
                break;
            case R.id.setting:
                startActivity(new Intent(this,Setting.class));
        }
        return true;
    }
}

