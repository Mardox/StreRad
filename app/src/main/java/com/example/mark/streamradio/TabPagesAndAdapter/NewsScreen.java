package com.example.mark.streamradio.TabPagesAndAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mark.streamradio.R;

/**
 * Created by kalyan on 1/10/14.
 */
public class NewsScreen extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radios, container, false);
    }
}
