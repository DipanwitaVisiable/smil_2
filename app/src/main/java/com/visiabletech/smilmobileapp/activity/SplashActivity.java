package com.visiabletech.smilmobileapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.MainActivity;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.Key;

public class SplashActivity extends Activity {

    String newVersion;
    String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Const.updateFalg = 1;
        setContentView(R.layout.activity_splash);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        /*stopService(new Intent(getBaseContext(), PushService.class));
        ShortcutBadger.removeCount(this);*/
        // Retrieved id.
        ImageView imageView = findViewById(R.id.imageView);


         // Logo animation.

        Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_screen_animation);
        imageView.startAnimation(anim);



        /*
          Thread for calling Login
          activity after 2 seconds.
         */

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                if (pref.getBoolean(Key.KEY_ACTIVITY_EXE, false)) {
                    Const.USER_ID = pref.getString(Key.KEY_STUDENT_ID, "");
                    Const.USER_ROLE = pref.getString(Key.KEY_USER_ROLE, "");
                    Const.PROFILENAME = pref.getString(Key.KEY_PROFILE_NAME, "");
                    Const.PhoneNo = pref.getString(Key.KEY_PHONE_NO, "");

                    Log.e("STUDENT ID ", Const.USER_ID);
                    Log.e("STUDENT ROLE ", Const.USER_ROLE);
                    Log.e("STUDENT NAME ", Const.PROFILENAME);
                    Log.e("STUDENT PHONE ", Const.PhoneNo);

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("GO TO LOGIN PAGE ", "YES");
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                SplashActivity.this.finish();
            }
        };
        handler.postDelayed(runnable, 2000);


    }
}