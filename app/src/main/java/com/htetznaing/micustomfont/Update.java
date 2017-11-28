package com.htetznaing.micustomfont;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.snatik.storage.Storage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by HtetzNaing on 11/27/2017.
 */

public class Update {
    Context context;
    ProgressDialog progressDialog;
    String nVersion, cVersion;
    PackageManager manager;
    PackageInfo info;
    String mainPath = "/sdcard/Android/data/com.htetznaing.micustomfont/";
    File myPath;
    InterstitialAd interstitialAd;
    AdRequest adRequest;

    public Update(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait!!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        setAD();

        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            cVersion = info.versionName;
            nVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        myPath = new File(context.getFilesDir(), "");
    }

    public void setAD(){
        adRequest = new AdRequest.Builder().build();

        interstitialAd = new InterstitialAd(context);
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

    public void startUpdate() {
        if (isInternetOn() == true) {
            DownloadFiles();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Attention!");
            builder.setMessage("No Internet connection :(\nYou need to connect Internet!");
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void DownloadFiles() {
        progressDialog.setMessage("Checking...");
        progressDialog.show();
        new DownloadFileFromURL().execute("https://github.com/KhunHtetzNaing/miMyanmarFont/releases/download/0/check.txt");
        Log.d("Download","https://github.com/KhunHtetzNaing/miMyanmarFont/releases/download/0/check.txt");
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                OutputStream output = new FileOutputStream(myPath + "/check");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            File myTest = new File(myPath.toString() + "/check");
            progressDialog.hide();
            if (myTest.exists()) {
                try {
                    nVersion = getStringFromFile(myTest.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                double nInt = Double.parseDouble(nVersion);
                double cInt = Double.parseDouble(cVersion);
                if (nVersion.equals(cVersion) == true || nInt<cInt) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Congratulations!");
                    builder.setMessage("You are on Latest Version ;)");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showAD();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Attention!");
                    builder.setMessage("New Version " + nVersion + " released\nDo you want to Upgrade ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DownloadUPDate();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } else {
                Toast.makeText(context, "Error Checking!", Toast.LENGTH_SHORT).show();
            }
        }

        public String getStringFromFile(String filePath) throws Exception {
            File fl = new File(filePath);
            FileInputStream fin = new FileInputStream(fl);
            String ret = convertStreamToString(fin);
            fin.close();
            return ret;
        }

        public String convertStreamToString(InputStream is) throws Exception {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("");
            }
            reader.close();
            return sb.toString();
        }

    }

    public void DownloadUPDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Please!");
        builder.setMessage("Choose Download From ?");
        builder.setPositiveButton("Play Store", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent apk = new Intent(Intent.ACTION_VIEW);
                String nnn = nVersion;
                nnn = nnn.replace(".", "_");
                apk.setData(Uri.parse("http://bit.ly/MiMyanmarFont_" + nnn));
                context.startActivity(apk);
                showAD();
            }
        });
        builder.setNegativeButton("Direct APK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isInternetOn()==true){
                    progressDialog.setMessage("Downloading...");
                    progressDialog.show();
                    dlAPK();
                    Toast.makeText(context, "Downloading Update in Background...", Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Attention!");
                    builder.setMessage("No Internet connection :(\nYou need to connect Internet!");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean createDir() {
        Storage storage = new Storage(context);
        boolean f = false;
        storage.createDirectory(mainPath);
        if (storage.isDirectoryExists(mainPath) == true) {
            f = true;
        }
        Log.d("Storage", "Created " + String.valueOf(f));
        return f;
    }

    public void dlAPK() {
        createDir();
        new DownloadAPKFromURL().execute("https://github.com/KhunHtetzNaing/miMyanmarFont/releases/download/0/MiMyanmarFont_v" + nVersion + ".apk");
        Log.d("Download","https://github.com/KhunHtetzNaing/miMyanmarFont/releases/download/0/MiMyanmarFont_v" + nVersion + ".apk");
    }

    class DownloadAPKFromURL extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                OutputStream output = new FileOutputStream(mainPath + "update"+nVersion+".apk");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            File myTest = new File(mainPath + "update"+nVersion+".apk");
            progressDialog.hide();
            showAD();
            if (myTest.exists()) {
                try {
                    File toInstall = new File(mainPath +"update"+nVersion+".apk");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                        intent.setData(apkUri);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(intent);
                    } else {
                        Uri apkUri = Uri.fromFile(toInstall);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    Toast.makeText(context, "Downloaded Update Please Install :)", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(context, "Error Installing", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public final boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}
