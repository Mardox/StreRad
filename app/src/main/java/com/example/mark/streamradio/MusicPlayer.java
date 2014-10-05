package com.example.mark.streamradio;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by User on 2014.07.03..
 */
public class MusicPlayer {
    private Context context;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private MusicPlayerTask musicPlayerTask = new MusicPlayerTask();
    private ConnectivityManager  cm;
    private NetworkInfo netInfo;
    private RadioListElement radioListElement;

    public MusicPlayer() {
        startListeners(mediaPlayer);
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void play(RadioListElement rle) {
        radioListElement=rle;
        musicPlayerTask.cancel(true);
        musicPlayerTask = new MusicPlayerTask();
        musicPlayerTask.execute(radioListElement.getUrl());
        context=radioListElement.getContext();

        SharedPreferences sharedpreferences = context.getSharedPreferences("currentRadio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("currentRadioUrl", radioListElement.getUrl());
        editor.commit();

        int iCount;
        SharedPreferences interstitialPref = context.getSharedPreferences("interstitialCount", Context.MODE_PRIVATE);
        if (interstitialPref.contains("iCount"))
        {
            iCount = interstitialPref.getInt("iCount", 0);
        } else {
            iCount = 0;
        }
        iCount++;
        SharedPreferences.Editor editor2 = interstitialPref.edit();
        editor2.putInt("iCount", iCount);
        editor2.commit();

        if (iCount %3 == 0){
            MainActivity.loadInterstitial();
        }
    }

    public void startListeners(MediaPlayer mediaPlayer){
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {

                switch(i){
                    case 702:
                        System.out.println("MEDIA_INFO_BUFFERING_END");
                        MainActivity.stopBufferingAnimation();
                        cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        }
                        else {
                            MainActivity.stopBufferingAnimation();
                            MainActivity.getRadioListLocation().setText("Internet connection error.");
                            MainActivity.getStartOrStopBtn().setImageResource(R.drawable.play);
                        }
                        break;
                    case 701:
                        System.out.println("MEDIA_INFO_BUFFERING_START");
                        MainActivity.startBufferingAnimation();
                        cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        }
                        else {
                            MainActivity.stopBufferingAnimation();
                            MainActivity.getRadioListLocation().setText("Internet connection error.");
                            MainActivity.getStartOrStopBtn().setImageResource(R.drawable.play);
                        }
                        break;
                }
                return true;
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
            if(i2!=-107){
                cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    MainActivity.stopBufferingAnimation();
                    MainActivity.getRadioListLocation().setText("The radio is probably offline.");
                    MainActivity.getStartOrStopBtn().setImageResource(R.drawable.play);
                }else{
                    MainActivity.stopBufferingAnimation();
                    MainActivity.getRadioListLocation().setText("Internet connection error.");
                    MainActivity.getStartOrStopBtn().setImageResource(R.drawable.play);
                }
            }
                return false;
            }
        });
    }

    public class MusicPlayerTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.setViewPagerSwitch();
            MainActivity.startBufferingAnimation();
            MainActivity.getStartOrStopBtn().setImageResource(R.drawable.pause);
        }

        @Override
        protected String doInBackground(final String... params) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(params[0]);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        MainActivity.stopBufferingAnimation();
                        MainActivity.getRadioListLocation().setText(radioListElement.getFrequency());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
