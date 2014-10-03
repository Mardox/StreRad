package com.example.mark.streamradio;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class RecordingsActivity extends Activity {

    ListView recordingsList;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        recordingsList = (ListView) findViewById(R.id.recording_list);

        ArrayList<String> recordList = new ArrayList<String>();
        try {
            File directory = Environment.getExternalStorageDirectory();

            file = new File(directory + "/My Recordings");
            File list[] = file.listFiles();
            for (File aList : list) {
                recordList.add(aList.getName());
            }

            Collections.sort(recordList, Collections.reverseOrder());

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


                String product = ((TextView) view).getText().toString();
                String fullPathAudio = file.getAbsolutePath() + "/" + product;

                File resultFile = new File(fullPathAudio);

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(resultFile), "audio/*");
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recordings, menu);
        return true;
    }
}
