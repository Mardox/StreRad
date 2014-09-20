package com.example.mark.streamradio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mark Bús on 2014.06.29..
 */
public class RadioList extends LinearLayout {

    private static ArrayList<RadioListElement> radioList;
    private static MusicPlayer mpt = new MusicPlayer();
    private static Dialog dialog;
    private static String lastTouchedradioListName;
    private static Vibrator vibrate;
    private TableLayout radioListUI;
    private GestureDetector mGestureDetector;


    public RadioList(Context context, ArrayList<RadioListElement> radioList, TableLayout radioListUI) {
        super(context);
        View.inflate(context, R.layout.fragment_radios, this);
        this.radioList = radioList;
        this.radioListUI = radioListUI;
        this.mGestureDetector = new GestureDetector(context, new GestureListener());
        this.vibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        declarateDialog();
    }

    public static void showDialog() {
        if (MainActivity.getUserRadios().contains(lastTouchedradioListName)) {
            vibrate.vibrate(10);
            dialog.show();
        }
    }

    public static void resetOldSelectedRadio() {
        for (final RadioListElement rle : radioList) {
            if (rle.isPlayBol())
                rle.setElementDefault();
        }
    }

    public static String selectedRadio() {
        for (final RadioListElement rle : radioList) {
            if (rle.isPlayBol())
                return rle.getName().toString();
        }
        return null;
    }

    public static void nextOrPreviousRadioStation(int action, final TextView mainRadioLocation, final TextView mainRadioName) throws Exception {
        int index = -1;
        for (final RadioListElement rle : radioList) {
            if (rle.isPlayBol())
                index = radioList.indexOf(rle);
        }
        switch (action) {
            case 1:
                index = index + 1;
                if (index < radioList.size()) {
                    resetOldSelectedRadio();
                    radioList.get(index).touchUP();
                    mainRadioLocation.setText(radioList.get(index).getName());
                    mainRadioName.setText(radioList.get(index).getFrequency());
                    mpt.play(radioList.get(index));
                }
                break;
            case -1:
                index = index - 1;
                if (index >= 0) {
                    resetOldSelectedRadio();
                    radioList.get(index).touchUP();
                    mainRadioLocation.setText(radioList.get(index).getName());
                    mainRadioName.setText(radioList.get(index).getFrequency());
                    mpt.play(radioList.get(index));
                }
                break;

            case 0:
                if (index != -1)
                    mpt.play(radioList.get(index));
                else
                    MainActivity.nextPage();
                break;
        }
    }

    public static void listeningReset(final TextView mainRadioLocation) throws Exception {
        int index = -1;
        for (final RadioListElement rle : radioList) {
            if (rle.getName().equals(mainRadioLocation.getText()))
                index = radioList.indexOf(rle);
        }
        radioList.get(index).touchUP();
    }

    public void declarateDialog() {
        dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_radio_dialog);
        dialog.getWindow().setLayout(600, 400);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Typeface fontRegular = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/font.otf");
        TextView dialogTitle = (TextView) dialog.getWindow().findViewById(R.id.dialogTitle);
        TextView dialogQuestion = (TextView) dialog.getWindow().findViewById(R.id.dialogQuestion);
        Button cancelBtn = (Button) dialog.getWindow().findViewById(R.id.cancel);
        Button confirmBtn = (Button) dialog.getWindow().findViewById(R.id.confirm);

        dialogTitle.setTypeface(fontRegular);
        dialogQuestion.setTypeface(fontRegular);
        cancelBtn.setTypeface(fontRegular);
        confirmBtn.setTypeface(fontRegular);

        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getDataManager().deleteExistingData(lastTouchedradioListName);
                dialog.dismiss();
                MainActivity.radioListRefresh();
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (RadioListElement rle : radioList) {
                    if (!rle.getName().equals(selectedRadio()) && rle.getName().equals(lastTouchedradioListName)) {
                        rle.touchCancel();
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                for (RadioListElement rle : radioList) {
                    if (!rle.getName().equals(selectedRadio()) && rle.getName().equals(lastTouchedradioListName)) {
                        rle.touchCancel();
                    }
                }
            }
        });
    }

    public void addRadioStations(final TextView mainRadioLocation, final TextView mainRadioName) {
        for (final RadioListElement rle : radioList) {
            rle.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    lastTouchedradioListName = rle.getName();
                    mGestureDetector.onTouchEvent(motionEvent);

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !rle.isPlayBol() && !dialog.isShowing()) {
                        rle.touchDown();
                    } else {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP && !rle.isPlayBol() && !dialog.isShowing()) {
                            resetOldSelectedRadio();
                            rle.touchUP();
                            mainRadioLocation.setText(rle.getName());
                            mainRadioName.setText(rle.getFrequency());
                            try {
                                mpt.play(rle);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL && !rle.isPlayBol()) {
                            rle.touchCancel();
                        }
                    }
                    return true;
                }
            });
            radioListUI.addView(rle);
        }
        try {
            listeningReset(mainRadioLocation);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

