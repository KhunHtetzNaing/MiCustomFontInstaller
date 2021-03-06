package com.htetznaing.micustomfont;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.snatik.storage.Storage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnNext,btnSkip;
    EditText editText;
    ImageView ivPreview;
    boolean ivHave = false;
    String old = "CustomFont";
    AdRequest adRequest;
    AdView banner;
    static SecondActivity secondActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        secondActivity = this;

        setAD();

        editText = (EditText) findViewById(R.id.edText);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);

        btnNext.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
    }


    public void setAD(){
        adRequest = new AdRequest.Builder().build();
        banner = (AdView) findViewById(R.id.adView);
        banner.loadAd(adRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSkip:;startActivity(new Intent(SecondActivity.this,ThirdActivity.class));btnSkip.setEnabled(false);break;
            case R.id.btnNext:
                final String lol = editText.getText().toString();
                if (lol.isEmpty()||lol.equals(null)){
                    Toast.makeText(this, "Please enter font name!", Toast.LENGTH_SHORT).show();
                }else{
                    if (ivHave==true) {
                        writeEdit(lol);
                    }else{
                        AlertDialog.Builder b = new AlertDialog.Builder(this);
                        b.setTitle("Attention!");
                        b.setMessage("Without font preview Image ?");
                        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                writeEdit(lol);
                            }
                        });
                        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(SecondActivity.this, "Please choose your theme preview image!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog d = b.create();
                        d.show();
                    }
                }
                break;
        }
    }

    public static SecondActivity getInstance(){
        return secondActivity;
    }

    public void writeEdit(String fName){
        Storage storage = new Storage(this);
        String fonts = "/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/meta/fonts/";
        String statusbar = "/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/meta/statusbar/";
        String theme = "/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/meta/theme/";

        //Edit Font Name
        File font1 = new File(fonts);
        File fontsFile[] = font1.listFiles();
        String fontFileName = fontsFile[0].toString();

        String fontsdata = storage.readTextFile(fontFileName);
        fontsdata = fontsdata.replace(old,fName);
        storage.deleteFile(fontFileName);
        storage.createFile(fontFileName,fontsdata);
        Log.d("Ogay",storage.readTextFile(fontFileName));

        //Edit Designer Name
        File status = new File(statusbar);
        File statusFile[] = status.listFiles();
        String statusFileName = statusFile[0].toString();
        String statusdata = storage.readTextFile(statusFileName);
        statusdata = statusdata.replace(old,fName);

        storage.deleteFile(statusFileName);
        storage.createFile(statusFileName,statusdata);
        Log.d("Ogay",storage.readTextFile(statusFileName));

        //Edit Author Name
        File themes = new File(theme);
        File themesFile[] = themes.listFiles();
        String themesFileName = themesFile[0].toString();
        String themesdata = storage.readTextFile(themesFileName);

        themesdata = themesdata.replace(old,fName);

        storage.deleteFile(themesFileName);
        storage.createFile(themesFileName,themesdata);
        Log.d("Ogay",storage.readTextFile(themesFileName));

        old = fName;
        startActivity(new Intent(SecondActivity.this,ThirdActivity.class));
        btnSkip.setEnabled(false);
    }

    public void browsePreview(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 071012);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 071012 && resultCode == RESULT_OK && data!=null) {
            Uri uri = data.getData();

            String filePath= null;
            try {
                filePath = new PathUtils().getPath(this,uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Log.d("ImagePath",filePath);
            String newIvPath = null;

            Storage storage = new Storage(this);
            File f = new File("/sdcard/Android/data/com.htetznaing.micustomfont/CustomFont/.data/preview/theme/");
            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    newIvPath = inFile.toString();
                }
            }

            storage.deleteFile(newIvPath+"/preview_lockscreen_0.jpg");
            storage.copy(filePath,newIvPath+"/preview_lockscreen_0.jpg");
            File n = new File(newIvPath+"/preview_lockscreen_0.jpg");
            if (n.exists()==true){
                ivPreview.setImageBitmap(BitmapFactory.decodeFile(newIvPath+"/preview_lockscreen_0.jpg"));
                ivHave = true;
            }else {
                Toast.makeText(this, "Something was wrong :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
