package com.visiabletech.smilmobileapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.CallStateListener;
import com.visiabletech.smilmobileapp.Utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomPlayerTwoActivity extends AppCompatActivity {

  private String video_url, video_id;
  private RequestQueue requestQueue;
  private YouTubePlayerView youTubePlayerView;
  public TelephonyManager tm;
  private CallStateListener callStateListener;
  private Context context;
  public static YouTubePlayer mYouTubePlayer;
  private Button btn_play;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_player_two);

    //Start- Register telephony manager to detect phone call
    context=CustomPlayerTwoActivity.this;
    callStateListener=new CallStateListener(context);
    tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    //End- Register telephony manager to detect phone call

    video_url=getIntent().getExtras().getString("url");
    video_id=getIntent().getExtras().getString("video_id");
    youTubePlayerView = findViewById(R.id.youtube_player_view);
    btn_play = findViewById(R.id.btn_play);
    initYouTubePlayerView();

    requestQueue = Volley.newRequestQueue(this);
    onlineAttendanceApiCall();
  }

  private void initYouTubePlayerView() {
    getLifecycle().addObserver(youTubePlayerView);
    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
      @Override
      public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        mYouTubePlayer=youTubePlayer;
        YouTubePlayerUtils.loadOrCueVideo (youTubePlayer, getLifecycle(), video_url,0f);
      }

      @Override
      public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
        super.onStateChange(youTubePlayer, state);

        if (state.name().trim().toString().equals("PAUSED"))
        {
          btn_play.setVisibility(View.VISIBLE);
        }
        else if (state.name().trim().toString().equals("PLAYING"))
        {
          btn_play.setVisibility(View.GONE);
        }
        else if (state.name().trim().toString().equals("ENDED"))
        {
          onBackPressed();
        }

      }

    });

    btn_play.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        btn_play.setVisibility(View.GONE);
        mYouTubePlayer.play();
      }
    });

  }

  private void onlineAttendanceApiCall() {
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "online_attendence", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          String status = jsonObject.getString("status");

          if (status.equalsIgnoreCase("200"))
          {

          }
          else if (status.equalsIgnoreCase("206"))
          {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.force_logout)
              .setCancelable(false)
              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  Intent intent = new Intent(context, LoginActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);
                }
              });
            AlertDialog alert = builder.create();
            alert.show();
          }
          else
          {
//                        Toast.makeText(context, "No Video found", Toast.LENGTH_SHORT).show();
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

                /*Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                pb_loader.setVisibility(View.GONE);*/


      }
    }

    ){

      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String> myParams = new HashMap<String, String>();
        myParams.put("student_id", Const.USER_ID);
//                myParams.put("student_id", "2623");
        myParams.put("days_id", Const.DAYS_ID);
        myParams.put("periods_id", Const.PERIOD_ID);
        myParams.put("video_id", video_id);
        return myParams;
      }
    };

    requestQueue.add(stringRequest);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mYouTubePlayer=null;
    tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
  }

}
