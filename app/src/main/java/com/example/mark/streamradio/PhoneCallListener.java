package com.example.mark.streamradio;

import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by User on 2014.07.11..
 */
class PhoneCallListener extends PhoneStateListener {
    private AudioManager audioManager;
    private int volume;
    private boolean notRunWhenStart = true;
    private boolean volumeStored = false;

    PhoneCallListener(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        if (notRunWhenStart)
            notRunWhenStart = false;
        else {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (volumeStored) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, volume);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (!volumeStored) {
                        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        volumeStored = true;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if (!volumeStored) {
                        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        volumeStored = true;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}