package com.htetznaing.micustomfont;

import android.content.Context;

/**
 * Created by HtetzNaing on 11/23/2017.
 */

public class Random {
    Context context;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    int check = 1;
    String Htetz1 = "91477193-6986-45b6-b748-3d1b9f989b53.mrc";

    public Random(Context context) {
        this.context = context;
//        sharedPreferences = context.getSharedPreferences("Htetz",context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
    }

    public String get(){
//        check = sharedPreferences.getInt("check",1);
//        Log.d("MgMg", String.valueOf(check));

        String lol = Htetz1;
//        switch (check){
//            case 1:lol = Htetz1;break;
//        }
        return lol;
    }
}
