package com.example.mark.streamradio;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by User on 2014.08.06..
 */
public class DataManager {

    private Context context;
    private String fileName;


    public DataManager(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void addNewRadio(String radioListName, String location, String URL) {
        if (loadStoredData() == null) {
            addRadio(radioListName, location, URL, loadStoredData());
        }
        addRadio(radioListName, location, URL, loadStoredData());
    }


    public boolean addRadio(String radioListName, String location, String URL, ArrayList<String> oldLines) {
        try {
            String oldText = "";
            FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            for (String line : oldLines) {
                String data[] = line.split("<separator>");
                if (data[0].toString().equals(radioListName.toString()))
                    System.out.println("Egyezik");
                else
                    oldText = oldText + "" + line + "\n";
            }
            oldText = oldText + "" + radioListName + "<separator>" + location + "<separator>" + URL;
            fOut.write(oldText.getBytes());
            fOut.close();
            return true;
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public boolean deleteExistingData(String radioListName) {
        try {
            ArrayList<String> oldLines = loadStoredData();
            String oldText = "";
            FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            for (int i = 0; i < oldLines.size(); i++) {
                String part[] = oldLines.get(i).split("<separator>");
                if (!part[0].equals(radioListName) && i < oldLines.size() - 1) {
                    oldText = oldText + "" + oldLines.get(i) + "\n";
                } else if (!part[0].equals(radioListName) && i == oldLines.size() - 1) {
                    oldText = oldText + "" + oldLines.get(i);
                }
            }
            fOut.write(oldText.getBytes());
            fOut.close();
            return true;
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public ArrayList<String> loadStoredData() {
        ArrayList<String> lineList = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
            String line;
            while ((line = br.readLine()) != null) {
                lineList.add(line);
            }
            br.close();
            return lineList;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public void loadStoredRadioStations(ArrayList<RadioListElement> radioList, ArrayList<String> userRadios) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
            String line;
            userRadios.clear();
            while ((line = br.readLine()) != null) {
                String data[] = line.split("<separator>");
                System.out.println(line);
                userRadios.add(data[0]);
                radioList.add(new RadioListElement(context, data[0], data[1], data[2], true));
            }
            br.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void createRadioListForRadioScreen(TableLayout UIRadioList,  ArrayList<String> userRadios, TextView radioListName, TextView radioListLocation) {
        ArrayList<RadioListElement> radioArray = new ArrayList<RadioListElement>();
        MainActivity.getDataManager().loadStoredRadioStations(radioArray, userRadios);
        radioArray.add(new RadioListElement(context, "My Recordings", "", ""));
        radioArray.add(new RadioListElement(context, "DEFJAY Radio", "USA", "http://tuner.defjay.com:80/"));
        radioArray.add(new RadioListElement(context, "Bridge FM", "Bridgend", "http://icy-e-04.sharp-stream.com:80/tcbridge.mp3"));
        radioArray.add(new RadioListElement(context, "Capital FM", "London", "http://ice-the.musicradio.com:80/CapitalMP3"));
        radioArray.add(new RadioListElement(context, "89.5 Music FM", "Budapest", "http://79.172.241.238:8000/musicfm.mp3"));
        radioArray.add(new RadioListElement(context, "1POWER", "New York", "http://powerhitz.powerhitz.com:5040/"));
        radioArray.add(new RadioListElement(context, "WFMU", "Only online", "http://stream0.wfmu.org/freeform-128k"));
        radioArray.add(new RadioListElement(context, "Slam FM", "Netherlands", "http://eu4.fastcast4u.com:5352/"));
        radioArray.add(new RadioListElement(context, "Pulse 87", "New York", "http://icy3.abacast.com/pulse87-pulse87aac-64"));
        UIRadioList.removeAllViews();
        RadioList radioList = new RadioList(context, radioArray, UIRadioList);
        radioList.addRadioStations(radioListName, radioListLocation);
    }

}





















