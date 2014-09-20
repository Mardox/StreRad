package com.example.mark.streamradio.TabPagesAndAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mark.streamradio.R;

/**
 * Created by Márk on 2014.02.23..
 */
public class RadiosScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View android = inflater.inflate(R.layout.fragment_radios, container, false);
        return android;
    }
}
