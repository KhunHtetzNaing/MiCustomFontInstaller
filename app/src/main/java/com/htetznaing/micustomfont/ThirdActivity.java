package com.htetznaing.micustomfont;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.snatik.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ThirdActivity extends AppCompatActivity {
    String mainPath = "/sdcard/Android/data/com.htetznaing.micustomfont/";
    TextView tvTitle;
    String ttile = "Your font name";
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    ImageView ivPreview;
    String textPath = "/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/meta/fonts/";
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        setAD();
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        setTvTitle();
        setIvPreview();
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

    public void setTvTitle(){
        Storage storage = new Storage(this);

        File f = new File(textPath);
        File nFile[] = f.listFiles();
        String tPath = nFile[0].toString();
        String text = storage.readTextFile(tPath);

        text = text.substring(text.indexOf("titles"),text.lastIndexOf("descriptions"));
        text = text.substring(text.indexOf("fallback"),text.lastIndexOf("en_US"));
        text = text.replace("fallback\":\"","");
        text = text.replace("\",\"","");

        tvTitle.setText(text);
        Log.d("Text",text);

        ttile = text;

        File ff = new File("/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/content/fonts/91477193-6986-45b6-b748-3d1b9f989b53.mrc");
        Typeface typeface = Typeface.createFromFile(ff);
        Converter converter = new Converter();
        bitmap = converter.textAsBitmap(text,50,0, Color.BLACK, typeface);
    }

    public void setIvPreview(){
        String ivPaht = null;
        String ivP = null;
        File f = new File("/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/preview/theme/");
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                ivPaht = inFile.toString()+"/preview_lockscreen_0.jpg";
                ivP = inFile.toString();
            }
        }

        Converter converter =new Converter();
        Storage storage = new Storage(this);
        storage.deleteFile(ivP+"/font_preview.jpg");
        converter.saveImage(bitmap,ivP+"/font_preview.jpg");

        File n = new File(ivPaht);
        if (n.exists()==true){
            ivPreview.setImageBitmap(BitmapFactory.decodeFile(ivPaht));
        }
    }

    public void goWork(){
        editFontSize();
        editSHA1();

        Fucker my = new Fucker();
        boolean lol = my.zipFileAtPath("/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/","/sdcard/Android/data/com.htetznaing.micustomfont/test.zip");
        my.unZip("/sdcard/Android/data/com.htetznaing.micustomfont/test.zip","/sdcard/MIUI/theme/");
        goSet();
    }

    public void editFontSize(){
        Storage storage = new Storage(this);
        Fucker my = new Fucker();

        File n = new File("/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/content/fonts/");
        File n1 []= n.listFiles();
        String fPath = n1[0].toString();

        String size = my.getBytes(this,fPath);
        Log.d("Size",size);

        //Edit Font Size
        String fonts = "/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/meta/fonts/";
        File font1 = new File(fonts);
        File fontsFile[] = font1.listFiles();
        String fontFileName = fontsFile[0].toString();

        String fontsdata = storage.readTextFile(fontFileName);
        String oldData = fontsdata;
        oldData = oldData.substring(oldData.indexOf("size"),oldData.lastIndexOf("updatedTime"));
        oldData = oldData.replace("size\":","");
        oldData = oldData.replace(",\"","");
        Log.d("Size",oldData);

        fontsdata = fontsdata.replace(oldData,size);
        storage.deleteFile(fontFileName);
        storage.createFile(fontFileName,fontsdata);
        Log.d("Ogay",storage.readTextFile(fontFileName));
    }

    public void editSHA1(){
        Storage storage = new Storage(this);
        Fucker my = new Fucker();
        File n = new File("/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/content/fonts/");
        File n1 []= n.listFiles();
        String fPath = n1[0].toString();

        String sha1 = null;
        try {
            sha1 = my.getSHA1(new File(fPath));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Edit Font SHA1
        String fonts = "/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/meta/fonts/";
        File font1 = new File(fonts);
        File fontsFile[] = font1.listFiles();
        String fontFileName = fontsFile[0].toString();

        String fontsdata = storage.readTextFile(fontFileName);
        String oldData = fontsdata;
        oldData = oldData.substring(oldData.indexOf("hash"),oldData.lastIndexOf("platform"));
        oldData = oldData.replace("hash\":\"","");
        oldData = oldData.replace("\",\"","");
        Log.d("OLD SHA1",oldData);

        fontsdata = fontsdata.replace(oldData,sha1);
        storage.deleteFile(fontFileName);
        storage.createFile(fontFileName,fontsdata);
        Log.d("Ogay",storage.readTextFile(fontFileName));


        // Edit font SHA1 in Rights

        String rfonts = "/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/rights/theme/";
        File rfont1 = new File(rfonts);
        File rfontsFile[] = rfont1.listFiles();
        String rfontFileName = rfontsFile[0].toString();

        String rfontsdata = storage.readTextFile(rfontFileName);
        Log.d("OldSHA",oldData);
        Log.d("NewSHA",sha1);
        rfontsdata = rfontsdata.replace(oldData,sha1);
        storage.deleteFile(rfontFileName);
        storage.createFile(rfontFileName,rfontsdata);
        Log.d("Ogay",storage.readTextFile(rfontFileName));
    }

    private void goSet() {
        Storage storage = new Storage(this);
        storage.deleteDirectory(mainPath + "CustomFont");
        storage.deleteFile(mainPath +"1.zip");
        storage.deleteFile("/sdcard/Android/data/com.htetznaing.micustomfont/test.zip");
        try {
            Toast.makeText(this, "Choose "+ttile+" > Apply", Toast.LENGTH_LONG).show();
            Intent localIntent = new Intent(Intent.ACTION_MAIN);
            localIntent.setComponent(new ComponentName("com.android.thememanager", "com.android.thememanager.activity.ThemeSettingsActivity"));
            startActivity(localIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Choose System font > "+ttile+" > Apply", Toast.LENGTH_LONG).show();
            Intent localIntent = new Intent(Intent.ACTION_MAIN);
            localIntent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$FontSettingsActivity"));
            startActivity(localIntent);
        }
        showAD();
        SecondActivity.getInstance().finish();
        finish();
    }

    public void startInstall(View view) {
        goWork();
    }
}
