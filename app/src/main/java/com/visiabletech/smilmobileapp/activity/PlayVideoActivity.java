package com.visiabletech.smilmobileapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.CallStateListener;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.YoutubeApiKey;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.CHROMELESS;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.DEFAULT;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.MINIMAL;

public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String video_url, video_id;
    private YouTubePlayerView youTubePlayerView;
    private RequestQueue requestQueue;

    public TelephonyManager tm;
    private CallStateListener callStateListener;
    private Context context;
    public static YouTubePlayer mYouTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Start- Register telephony manager to detect phone call
        context=PlayVideoActivity.this;
        callStateListener=new CallStateListener(context);
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //End- Register telephony manager to detect phone call

        video_url=getIntent().getExtras().getString("url");
        video_id=getIntent().getExtras().getString("video_id");
        youTubePlayerView=findViewById(R.id.youtube_player);
        youTubePlayerView.setEnabled(false);
        youTubePlayerView.initialize(YoutubeApiKey.YOUTUBE_API_KEY_PART_1 + YoutubeApiKey.YOUTUBE_API_KEY_PART_2 + YoutubeApiKey.YOUTUBE_API_KEY_PART_3 + YoutubeApiKey.YOUTUBE_API_KEY_PART_4,this);

        requestQueue = Volley.newRequestQueue(this);
        onlineAttendanceApiCall();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        this.mYouTubePlayer=youTubePlayer;
        mYouTubePlayer.loadVideo(video_url);
        mYouTubePlayer.setPlayerStyle(DEFAULT);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
