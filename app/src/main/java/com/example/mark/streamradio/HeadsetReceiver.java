package com.example.mark.streamradio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by User on 2014.07.10..
 */
public class HeadsetReceiver extends BroadcastReceiver {
    private Context context;
    private ToastCreator toastCreator;
    private boolean notRunWhenStart = true;

    public HeadsetReceiver(Context context) {
        this.context = context;
        this.toastCreator = new ToastCreator(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (notRunWhenStart)
            notRunWhenStart = false;
        else {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        toastCreator.show(R.drawable.volume_muted_light, "Headset unplugged");
                        MusicPlayer.getMediaPlayer().stop();
                        MainActivity.getStartOrStopBtn().setImageResource(R.drawable.play);
                        break;
                    case 1:
                        toastCreator.show(R.drawable.headphones, "Headset plugged");
                        break;
                    default:
                }
            }
        }
    }
}
