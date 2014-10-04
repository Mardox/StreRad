package com.example.mark.streamradio;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2014.07.21..
 */
public class MusicPlayerControl {
    private ImageView previous, stopOrStart, next, record;
    private TextView mainRadioLocation, mainRadioName;
    private MediaPlayer mediaPlayer;
    private Context context;
    private MediaRecorder recorder;
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    Dialog dialog;

    public MusicPlayerControl(ImageView previous, ImageView stopOrStart, ImageView next, TextView mainRadioName, TextView mainRadioLocation, ImageView record, Context context) {
        this.previous = previous;
        this.stopOrStart = stopOrStart;
        this.next = next;
        this.mainRadioName = mainRadioName;
        this.mainRadioLocation = mainRadioLocation;
        this.mediaPlayer = MusicPlayer.getMediaPlayer();
        this.record = record;
        this.context = context;
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

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    //Toast.makeText(context,"Clicked",Toast.LENGTH_LONG).show();
                    //Record();

                    if (mediaPlayer.isPlaying()) {
                        new RecordAudio().execute();
                        // Create custom dialog object

                        dialog = new Dialog(context);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.record_dialog);
                        // Set dialog title
                        dialog.setTitle("Please wait...");

                        // set values for custom dialog components - text, image and button
                        dialog.show();

                        Button stopButton = (Button) dialog.findViewById(R.id.recordStop);
                        // if decline button is clicked, close the custom dialog
                        stopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Close dialog
                                Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (fileOutputStream != null) {
                                    try {
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        fileOutputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //recorder.stop();
                            }
                        });
                    }else {
                        Toast.makeText(context, "Please start a radio to record!", Toast.LENGTH_LONG).show();
                    }


                }
                return true;
            }
        });
    }


    public class RecordAudio extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            String dateInString = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            String fileName = "StreamRadio_" + dateInString + " record.mp3";

            String SDCardpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/My Recordings";

            File myDataPath = new File(SDCardpath);

            // mydir = context.getDir("media", Context.MODE_PRIVATE);
            if (!myDataPath.exists()) {
                myDataPath.mkdir();
            }


            File audiofile = new File(myDataPath + "/" + fileName);
            try {
                //URL url = new URL("http://icy-e-04.sharp-stream.com:80/tcbridge.mp3");
                String tempUrl;
                SharedPreferences sharedpreferences = context.getSharedPreferences("currentRadio", Context.MODE_PRIVATE);
                if (sharedpreferences.contains("currentRadioUrl"))
                {
                    tempUrl = sharedpreferences.getString("currentRadioUrl", "");
                } else {
                    tempUrl = null;
                }
                URL url = new URL(tempUrl);
                inputStream = url.openStream();
                fileOutputStream = new FileOutputStream(audiofile,false);
                int c;
                while ((c = inputStream.read()) != -1) {
                    fileOutputStream.write(c);
                    publishProgress(c);
                }

            }catch (Exception e){
                //Log.d("Recording the stream", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progUpdate) {
            dialog.setTitle("Recording...");
        }
    }
}
