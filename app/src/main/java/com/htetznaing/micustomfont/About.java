package com.htetznaing.micustomfont;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class About extends AppCompatActivity {
    AdRequest adRequest;
    AdView banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setAD();
    }

    public void dev(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse("fb://profile/100011339710114"));
            startActivity(intent);
        } catch (Exception e) {
            intent.setData(Uri.parse("https://m.facebook.com/KHtetzNaing"));
            startActivity(intent);
        }
    }

    public void setAD(){
        adRequest = new AdRequest.Builder().build();
        banner = (AdView) findViewById(R.id.adView);
        banner.loadAd(adRequest);
    }

    public void update(View view) {
        if (checkPermissions()==true){
            Update update = new Update(About.this);
            update.startUpdate();
        }
    }

    private boolean checkPermissions() {
        int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            Log.d("TAG","Permission"+"\n"+String.valueOf(false));
            return false;
        }
        Log.d("Permission","Permission"+"\n"+String.valueOf(true));
        return true;
    }
}
