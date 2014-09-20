package com.example.mark.streamradio;

import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 2014.07.21..
 */
public class MusicPlayerControl {
    private ImageView previous, stopOrStart, next;
    private TextView mainRadioLocation, mainRadioName;
    private MediaPlayer mediaPlayer;

    public MusicPlayerControl(ImageView previous, ImageView stopOrStart, ImageView next, TextView mainRadioName, TextView mainRadioLocation) {
        this.previous = previous;
        this.stopOrStart = stopOrStart;
        this.next = next;
        this.mainRadioName = mainRadioName;
        this.mainRadioLocation = mainRadioLocation;
        this.mediaPlayer = MusicPlayer.getMediaPlayer();
    }

    public void setOnTouchListeners() {
        previous.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        RadioList.nextOrPreviousRadioStation(-1, mainRadioLocation, mainRadioName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        RadioList.nextOrPreviousRadioStation(1, mainRadioLocation, mainRadioName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        stopOrStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        MainActivity.getStartOrStopBtn().setImageResource(R.drawable.play);
                    } else if (LoadingAnimation.hasEnded()) {
                        try {
                            RadioList.nextOrPreviousRadioStation(0, mainRadioLocation, mainRadioName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });
    }

}
