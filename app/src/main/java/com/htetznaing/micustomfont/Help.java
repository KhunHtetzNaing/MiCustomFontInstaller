package com.htetznaing.micustomfont;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Help extends AppCompatActivity {
    EditText edText;
    AdRequest adRequest;
    AdView banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setAD();

        edText = (EditText) findViewById(R.id.edText);
    }

    public void Send(View view) {
        if (edText.getText().equals(null) || edText.getText().toString().isEmpty()){
            Toast.makeText(this, "Please write your want to say!!", Toast.LENGTH_SHORT).show();
        }else{
            sentemail();
        }
    }

    public void setAD(){
        adRequest = new AdRequest.Builder().build();
        banner = (AdView) findViewById(R.id.adView);
        banner.loadAd(adRequest);
    }

    public void sentemail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "khunhtetznaing@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MIUI Custom Font Installer");
        emailIntent.putExtra(Intent.EXTRA_TEXT, edText.getText().toString());
        startActivity(Intent.createChooser(emailIntent, "Send email via..."));
    }
}
