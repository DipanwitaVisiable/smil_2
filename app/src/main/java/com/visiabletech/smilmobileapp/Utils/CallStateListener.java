package com.visiabletech.smilmobileapp.Utils;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.visiabletech.smilmobileapp.activity.CustomPlayerTwoActivity;
import com.visiabletech.smilmobileapp.activity.PlayVideoActivity;
import com.visiabletech.smilmobileapp.activity.SmallQuizTestActivity;

import static android.widget.Toast.LENGTH_LONG;

public class CallStateListener extends PhoneStateListener {
    private Context context;

    public CallStateListener(Context context) {
        this.context=context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:

                if (SmallQuizTestActivity.countDownTimer!=null)
                  SmallQuizTestActivity.countDownTimer.pause();

                /*if (PlayVideoActivity.mYouTubePlayer!=null)
                    PlayVideoActivity.mYouTubePlayer.pause();*/

                if (CustomPlayerTwoActivity.mYouTubePlayer!=null)
                    CustomPlayerTwoActivity.mYouTubePlayer.pause();
                break;

            case TelephonyManager.CALL_STATE_IDLE:

                if (SmallQuizTestActivity.countDownTimer!=null)
                    SmallQuizTestActivity.countDownTimer.resume();

                /*if (PlayVideoActivity.mYouTubePlayer!=null)
                    PlayVideoActivity.mYouTubePlayer.play();*/

                if (CustomPlayerTwoActivity.mYouTubePlayer!=null)
                    CustomPlayerTwoActivity.mYouTubePlayer.play();
                break;
        }
    }
}
