package com.htetznaing.micustomfont;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nononsenseapps.filepicker.Utils;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String mainPath = "/sdcard/Android/data/com.htetznaing.micustomfont/";
    String Htetz;
    String font;
    String TAG = "CustomFont";
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;

    Button btnChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAD();
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(this);
    }

    public void setAD(){
        adRequest = new AdRequest.Builder().build();
        banner = (AdView) findViewById(R.id.adView);
        banner.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2502552457553139/5632694812");
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                loadAD();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                loadAD();
            }

            @Override
            public void onAdOpened() {
                loadAD();
            }
        });
    }

    public void loadAD(){
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }
    }

    public void showAD(){
        if (interstitialAd.isLoaded()){
            interstitialAd.show();
        }else{
            interstitialAd.show();
        }
    }

    @Override
    public void onClick(View view) {

        if (checkPermissions()==true) {
            Intent i = new Intent(this, FilePicker.class);
            startActivityForResult(i, 5217);
        }else{
            Toast.makeText(this, "Please allow Write External Storage Permission!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        showAD();
        if (requestCode == 5217 && resultCode == Activity.RESULT_OK) {
            List<Uri> files = Utils.getSelectedFilesFromResult(intent);
            for (Uri uri: files) {
                File file = Utils.getFileForUri(uri);
                font = file.toString();

                if (font.endsWith(".ttf") || font.endsWith(".mtz")) {

                    if (font.endsWith(".ttf")) {
                        Log.d("FileType",".ttf");
                        if (createDir() == true) {
                            install();
                        }
                    }

                    if (font.endsWith(".mtz")){
                        Log.d("FileType",".mtz");
                        if (createDir()==true){
                            if(extractMTZ()==true) {
                                install();
                            }
                        }
                    }

                }else{
                    Toast.makeText(this, "Please choose .ttf or .mtz files only :)", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean extractMTZ(){
        Storage storage = new Storage(this);
        Fucker my = new Fucker();
        storage.copy(font,mainPath+"mtz.zip");
        my.unZip(mainPath+"mtz.zip",mainPath+"mtz");
        storage.deleteFile(mainPath+"mtz.zip");

        File n = new File(mainPath+"mtz/fonts/");

        if (n.exists()) {
            File f[] = n.listFiles();
            for (int i = 0; i < f.length; i++) {
                String lol = f[i].toString();
                if (lol.endsWith(".ttf")) {
                    font = lol;
                }
            }
            return true;
        }else{
            storage.deleteDirectory(mainPath+"mtz");
            Toast.makeText(this, "Not found font file in your MTZ!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void install() {
//        sharedPreferences = getSharedPreferences("Htetz",MODE_PRIVATE);
//        int check = sharedPreferences.getInt("check",1);
        String fileLol = "1.zip";
        Log.d("Zip Name",fileLol);
        Storage storage = new Storage(this);
        Fucker my = new Fucker();
        my.assets2SD(this,fileLol,mainPath,fileLol);
        my.unZip(mainPath + fileLol, mainPath + "CustomFont");

        String fontPath = mainPath+"CustomFont/";
        storage.deleteFile(fontPath+".data/content/fonts/test");
        storage.copy(font,fontPath+".data/content/fonts/"+Htetz);

        if (storage.isFileExist(fontPath+".data/content/fonts/"+Htetz)==true){
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        storage.deleteDirectory(mainPath+"mtz");
    }

    private boolean createDir() {
        Random random = new Random(this);
        Htetz = random.get();
        Log.d("TAG","ID"+"\n"+Htetz);

        Storage storage = new Storage(this);
        boolean f = false;
        storage.createDirectory(mainPath);
        storage.createDirectory("/sdcard/MIUI/theme/.data/content/statusbar");
        storage.createDirectory("/sdcard/MIUI/theme/.data/meta/statusbar");
        storage.createDirectory("/sdcard/MIUI/theme/.data/content/theme");
        storage.createDirectory("/sdcard/MIUI/theme/.data/meta/theme");
        storage.createDirectory("/sdcard/MIUI/theme/.data/rights/theme");
        storage.createDirectory("/sdcard/MIUI/theme/.data/preview/theme");
        storage.createDirectory("/sdcard/MIUI/theme/.data/content/fonts");
        storage.createDirectory("/sdcard/MIUI/theme/.data/meta/fonts");
        if (storage.isDirectoryExists(mainPath) == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/content/statusbar") == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/meta/statusbar") == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/content/theme") == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/meta/theme") == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/rights/theme") == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/preview/theme") == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/content/fonts") == true && storage.isDirectoryExists("/sdcard/MIUI/theme/.data/meta/fonts") == true) {
            f = true;
        }
        Log.d("TAG", "dirCreated " + String.valueOf(f));
        return f;
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

    public void About(View view) {
        showAD();
        startActivity(new Intent(MainActivity.this,About.class));
    }

    public void help(View view) {
        showAD();
        startActivity(new Intent(MainActivity.this,Help.class));
    }
}
