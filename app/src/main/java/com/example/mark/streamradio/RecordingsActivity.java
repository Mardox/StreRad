package com.example.mark.streamradio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.revmob.RevMob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class RecordingsActivity extends Activity {
    private RevMob revmob;
    ListView recordingsList;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        //ChartBoost
        String appId = "";
        String appSignature = "";
        Chartboost.startWithAppId(this, appId, appSignature);
        Chartboost.onCreate(this);

        recordingsList = (ListView) findViewById(R.id.recording_list);

        final ArrayList<String> recordList = new ArrayList<String>();
        final ArrayList<String> recordPathList = new ArrayList<String>();
        //String[] recordPathList = null;
        try {
            File directory = Environment.getExternalStorageDirectory();

            file = new File(directory + "/My Recordings");
            File list[] = file.listFiles();
            for (File aList : list) {
                recordList.add(aList.getName());
                recordPathList.add(aList.getAbsolutePath());
            }

            //Collections.sort(recordList, Collections.reverseOrder());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.news_list_item, R.id.newsListName,
                    recordList);


            recordingsList.setAdapter(adapter);
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(),"No recordings available!", Toast.LENGTH_LONG).show();
        }
        recordingsList.setTextFilterEnabled(true);

        recordingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                MediaPlayer mediaPlayer = new MediaPlayer();
                String path = recordPathList.get(position);
                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (RandomNumber()){
                    case 0:
                        revmob = RevMob.start(RecordingsActivity.this);
                        revmob.showFullscreen(RecordingsActivity.this);
                        break;
                    case 1:
                        Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
                        Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
                        break;
                    case 2:
                        //Do Nothing
                        break;
                    default:
                        //Do Nothing
                        break;
                }

//                String product = ((TextView) view).getText().toString();
//                String fullPathAudio = file.getAbsolutePath() + "/" + product;
//
//                File resultFile = new File(fullPathAudio);
//
//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(resultFile), "audio/*");
//                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recordings, menu);
        return true;
    }

    private int RandomNumber(){
        Random r = new Random();
        return r.nextInt(2);
    }

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it.
        if (Chartboost.onBackPressed())
            return;
        else
            super.onBackPressed();
    }
}
