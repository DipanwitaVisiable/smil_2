package com.visiabletech.smilmobileapp.update_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.visiabletech.smilmobileapp.MainActivity;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.activity.SplashActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

    private String latestVersion;
    private String currentVersion;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    private AlertDialog alertDialog;
    boolean isClick;

    public ForceUpdateAsync(String currentVersion, Context context){
        this.currentVersion = currentVersion;
        this.context = context;


    }

    @Override
    protected JSONObject doInBackground(String... params) {

        try {
            latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="+context.getPackageName()+"&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(latestVersion!=null){
            //Log.e("VERSION ", latestVersion + ", "+ currentVersion);
            if(!currentVersion.equalsIgnoreCase(latestVersion)){
                //Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                if(!(context instanceof SplashActivity)) {
                    if(!((Activity)context).isFinishing()){
                        showForceUpdateDialog();
                    }
                }
            }
        }
        super.onPostExecute(jsonObject);
    }

    private void showForceUpdateDialog(){

        AlertDialog.Builder alertDialogBuilder = alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                R.style.Theme_AppCompat_Dialog_Alert));
        alertDialogBuilder.setTitle("Update Available");
        alertDialogBuilder.setMessage("A new version of SMIL 2 is available. Please update");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                isClick=false;

            }
        });
        // alertDialogBuilder.show();

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
}
