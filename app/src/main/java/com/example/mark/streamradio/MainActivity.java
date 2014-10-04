package com.example.mark.streamradio;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.mark.streamradio.TabPagesAndAdapter.MainScreen;
import com.example.mark.streamradio.TabPagesAndAdapter.TabPagerAdapter;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    private static DataManager dataManager;
    private static TableLayout UIRadioList;
    private static ArrayList<String> userRadios = new ArrayList<String>();
    private static ViewPager viewPager;
    private static ImageView bufferingIndicator, speaker, startOrStopBtn, now_playing_icon, radio_icon, news_icon, more_icon, bottom0, bottom1, bottom2, bottom3;
    private static LoadingAnimation bufferingAnimation;
    private static AudioManager audioManager;
    private static TextView radioListLocation, radioListName, radioTitle;
    //private ImageView screenChaneButton, plus;
    private boolean runOnce = true;
    private LinearLayout volumeLayout, volumeButton;
    private int volumeStore;
    private ImageView previousBtn, nextBtn, recordBtn;
    private AdView adView;
    private Typeface fontRegular;

    public static void radioListRefresh() {
        dataManager.createRadioListForRadioScreen(UIRadioList, userRadios, radioListName, radioListLocation);
    }

    public static void setViewPagerSwitch() {
        viewPager.setCurrentItem(0, true);
    }

    public static void startBufferingAnimation() {
        bufferingIndicator = MainScreen.getLoadingImage();
        bufferingAnimation = new LoadingAnimation(bufferingIndicator);
        bufferingAnimation.startAnimation();
    }

    public static void stopBufferingAnimation() {
        bufferingIndicator = MainScreen.getLoadingImage();
        bufferingAnimation.clearAnimation();
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    public static ArrayList<String> getUserRadios() {
        return userRadios;
    }

    public static TextView getRadioListLocation() {
        return radioListLocation;
    }

    public static ImageView getStartOrStopBtn() {
        return startOrStopBtn;
    }

    public static void nextPage() {
        viewPager.setCurrentItem(1, true);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



        dataManager = new DataManager(this, "user_radio");
        fontRegular = Typeface.createFromAsset(getAssets(), "fonts/font.otf");
        //radioTitle = (TextView) findViewById(R.id.radioTitle);
        //radioTitle.setTypeface(fontRegular);





        TabPagerAdapter tabPageAdapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(tabPageAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
//                switch (viewPager.getCurrentItem()){
//                    case 0:
//                        AlterVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
//                        break;
//                    case 1:
//                        AlterVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
//                        break;
//                    case 2:
//                        AlterVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
//                        break;
//                }
                getActionBar().setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        final ActionBar actionBar = getActionBar();

        // Specify that tabs should be displayed in the action bar.
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        if (actionBar != null) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Now Playing")
                            .setTabListener(tabListener));
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Radio")
                            .setTabListener(tabListener));
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("News")
                            .setTabListener(tabListener));
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("More")
                            .setTabListener(tabListener));
        }


//        screenChaneButton = (ImageView) findViewById(R.id.nextScreen);
//        screenChaneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (viewPager.getCurrentItem() == 0) viewPager.setCurrentItem(1, true);
//                else viewPager.setCurrentItem(0, true);
//            }
//        });

        speaker = (ImageView) findViewById(R.id.speaker);
        speaker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeStore, volumeStore);
                    defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
                } else {
                    volumeStore = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
                }
                return false;
            }
        });
        volumeLayout = (LinearLayout) findViewById(R.id.linearLayout_t);
        volumeButton = (LinearLayout) findViewById(R.id.button_t);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        registerReceiver(new HeadsetReceiver(getApplicationContext()), new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneCallListener(audioManager), PhoneStateListener.LISTEN_CALL_STATE);

        previousBtn = (ImageView) findViewById(R.id.previous_btn);
        nextBtn = (ImageView) findViewById(R.id.next_btn);
        startOrStopBtn = (ImageView) findViewById(R.id.start_or_stop_btn);
        recordBtn = (ImageView) findViewById(R.id.record_btn);


//        adView = (AdView) this.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//        adView.setVisibility(View.INVISIBLE);
    }


    private void AlterVisibility(int V1, int V2, int V3, int V4){
        bottom0.setVisibility(V1);
        bottom1.setVisibility(V2);
        bottom2.setVisibility(V3);
        bottom3.setVisibility(V4);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (runOnce) {
            UIRadioList = (TableLayout) findViewById(R.id.radioListUi);
            radioListName = (TextView) findViewById(R.id.mainRadioName);
            radioListLocation = (TextView) findViewById(R.id.mainRadioLocation);
            startWallpaperAnimation();
            radioListRefresh();
            volumeBarReaction(volumeLayout, volumeButton, audioManager);
            new MusicPlayerControl(previousBtn, startOrStopBtn, nextBtn, radioListLocation, radioListName, recordBtn, this).setOnTouchListeners();
            connectionDialog(isOnline());
            runOnce = false;

        }
        defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
    }

    private void startWallpaperAnimation() {
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        LinearLayout picture = (LinearLayout) findViewById(R.id.wallpaper);
        TranslateAnimation animation = new TranslateAnimation(0, 0 - (picture.getWidth() - size.x), 0, 0);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200000);
        animation.setFillAfter(true);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        picture.startAnimation(animation);
    }

    public void defaultVolumeBarPosition(AudioManager audioManager, LinearLayout volumeLayout, LinearLayout volumeButton) {
        float actual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float endPoint = volumeLayout.getWidth() - volumeButton.getWidth();
        volumeButton.setX((endPoint / max * actual));
        if (volumeButton.getX() == 0)
            speaker.setImageResource(R.drawable.volume_muted);
        else speaker.setImageResource(R.drawable.volume_on);
    }

    public void volumeBarReaction(final LinearLayout volumeLayout, final LinearLayout volumeButton, final AudioManager audioManager) {

        volumeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float endPoint = volumeLayout.getWidth() - volumeButton.getWidth();
                volumeButton.setX(motionEvent.getX() - volumeButton.getWidth() / 2);

                if (volumeButton.getX() >= 0) {
                    float pos = volumeButton.getX() / (endPoint / max);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) pos, 0);
                }
                if (volumeButton.getX() >= endPoint) {
                    volumeButton.setX(endPoint);
                }
                if (volumeButton.getX() <= 0) {
                    volumeButton.setX(0);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    speaker.setImageResource(R.drawable.volume_muted);
                } else speaker.setImageResource(R.drawable.volume_on);
                return true;
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else return false;
    }

    public void connectionDialog(boolean isOnline) {
        if (!isOnline) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.without_internet);
            FrameLayout mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
            dialog.getWindow().setLayout((int) (mainLayout.getWidth() * 0.8), (int) (mainLayout.getHeight() * 0.45));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button retryBtn = (Button) dialog.getWindow().findViewById(R.id.retry);
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isOnline()) {
                        dialog.dismiss();
                        dialog.show();
                    } else dialog.dismiss();
                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (!isOnline()) {
                        dialog.dismiss();
                        dialog.show();
                    }
                }
            });
            dialog.show();
        }
    }

    public class FileToUrl extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String radioURL = "empty";
            try {
                URL url = new URL(strings[0]);
                URLConnection uc = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    if (inputLine.contains("http://")) {
                        String[] fields = inputLine.split("http://");
                        radioURL = "http://" + fields[1];
                        in.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("ERED: " + radioURL);
            return radioURL;
        }
    }

}


